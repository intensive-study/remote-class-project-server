package org.server.remoteclass.service.purchase;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.dto.order.RequestCancelPartialPurchaseDto;
import org.server.remoteclass.dto.order.RequestOrderLectureDto;
import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.dto.purchase.ResponsePurchaseByAdminDto;
import org.server.remoteclass.dto.purchase.ResponsePurchaseDto;
import org.server.remoteclass.entity.*;
import org.server.remoteclass.exception.BadRequestArgumentException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.jpa.*;
import org.server.remoteclass.util.AccessVerification;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService{

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final OrderRepository orderRepository;
    private final OrderLectureRepository orderLectureRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final FixDiscountCouponRepository fixDiscountCouponRepository;
    private final RateDiscountCouponRepository rateDiscountCouponRepository;
    private final PurchaseRepository purchaseRepository;
    private final AccessVerification accessVerification;
    private final ModelMapper modelMapper;

    @Autowired
    public PurchaseServiceImpl(UserRepository userRepository,
                               LectureRepository lectureRepository,
                               StudentRepository studentRepository,
                               OrderRepository orderRepository,
                               OrderLectureRepository orderLectureRepository,
                               PurchaseRepository purchaseRepository,
                               IssuedCouponRepository issuedCouponRepository,
                               FixDiscountCouponRepository fixDiscountCouponRepository,
                               RateDiscountCouponRepository rateDiscountCouponRepository,
                               BeanConfiguration beanConfiguration,
                               AccessVerification accessVerification){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.orderRepository = orderRepository;
        this.orderLectureRepository = orderLectureRepository;
        this.issuedCouponRepository = issuedCouponRepository;
        this.fixDiscountCouponRepository = fixDiscountCouponRepository;
        this.rateDiscountCouponRepository = rateDiscountCouponRepository;
        this.purchaseRepository = purchaseRepository;
        this.modelMapper = beanConfiguration.modelMapper();
        this.accessVerification = accessVerification;
    }

    @Override
    @Transactional
    public void createPurchase(RequestPurchaseDto requestPurchaseDto) {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        //orderId를 입력하면 order 객체 받아오기
        Order order = orderRepository.findById(requestPurchaseDto.getOrderId())
                .orElseThrow(() -> new IdNotExistException("해당 주문이 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));

        // pending상태만 주문 가능. 취소되거나 이미 완료된 주문이면 더이상 구매하지 못함.
        if(order.getOrderStatus() == OrderStatus.PENDING){
            Purchase purchase = modelMapper.map(requestPurchaseDto, Purchase.class);
            order.setOrderStatus(OrderStatus.COMPLETE);//해당 orderId의 주문에는 status를 complete로 변경하기
            purchase.setOrder(order);
            purchase.setValidPurchase(true);
            if(order.getSalePrice() == null){ // 할인가격이 없으면 원가만
                purchase.setPurchasePrice(order.getOriginalPrice());
            }
            else{ //할인가격이 있으면 할인가격이 최종가격으로
                purchase.setPurchasePrice(order.getSalePrice());
            }
            purchaseRepository.save(purchase);
            /**
             * 구매가 완료되면 수강생 테이블에 자동으로 삽입되어야함.
             */
            for(OrderLecture orderLecture : order.getOrderLectures()){
                if(studentRepository.existsByLecture_LectureIdAndUser_UserId(orderLecture.getLecture().getLectureId(), user.getUserId())) {
                    throw new BadRequestArgumentException("이미 수강하고 있는 강의입니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
                }
                else{
                    Student student = new Student();
                    student.setUser(user);
                    Lecture lecture = lectureRepository.findById(orderLecture.getLecture().getLectureId())
                            .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의입니다.", ErrorCode.ID_NOT_EXIST));
                    student.setLecture(lecture);
                    student.setOrder(order);
                    studentRepository.save(student);
                }
            }
        }
        else{
            throw new BadRequestArgumentException("주문 가능한 상태가 아닙니다", ErrorCode.BAD_REQUEST_ARGUMENT);
        }

    }

    // 강의 부분 수강 취소
    @Override
    @Transactional
    public void cancelPartialPurchases(Long purchaseId, RequestCancelPartialPurchaseDto requestCancelPartialPurchaseDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        //이전 주문번호와 이전 구매내역 모두 찾기
        Optional<Purchase> prevPurchase = purchaseRepository.findById(purchaseId);
        Optional<Order> prevOrder = orderRepository.findById(prevPurchase.get().getOrder().getOrderId());

        if(prevPurchase.get().isValidPurchase() == false){
            throw new BadRequestArgumentException("취소 가능한 구매번호가 아닙니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
        }
        if(prevOrder.get().getOrderStatus() != OrderStatus.COMPLETE){
            throw new BadRequestArgumentException("취소 가능한 주문 상태가 아닙니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
        }

        //주문의 강의리스트
        List<Long> lectureList = new ArrayList<>();

        //취소 요청한 강의가 있으면 이전 주문의 강의들을 모두 lectureList에 저장
        for(OrderLecture orderLecture: prevOrder.get().getOrderLectures()){
            lectureList.add(orderLecture.getLecture().getLectureId());
        }

        for(RequestOrderLectureDto requestOrderLectureDto : requestCancelPartialPurchaseDto.getOrderLectures()){
            Optional<Lecture> lecture = lectureRepository.findById(requestOrderLectureDto.getLectureId());
            //취소할 강의리스트 중 강의가 신청 안했던 강의이면 x
            if(!studentRepository.existsByLecture_LectureIdAndUser_UserId(requestOrderLectureDto.getLectureId(), user.getUserId())){
                throw new IdNotExistException("수강 취소할 수 없습니다.", ErrorCode.ID_NOT_EXIST);
            }
            // 강의가 이미 시작했던 강의이면 x
            if(lecture.get().getStartDate().isBefore(LocalDateTime.now())){
                throw new BadRequestArgumentException("이미 시작한 강의는 취소할 수 없습니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
            }
            //이전 주문의 강의리스트에서 삭제할 강의 하나씩 삭제
            lectureList.remove(Long.valueOf(requestOrderLectureDto.getLectureId()));
        }

        //모두 삭제했을 경우(5개 중 5개 모두 입력했을 경우)
        if(lectureList.isEmpty()){
            prevOrder.ifPresent(order -> order.setOrderStatus(OrderStatus.CANCEL));
            prevPurchase.ifPresent(purchase -> purchase.setValidPurchase(false));
            studentRepository.deleteByUser_UserIdAndOrder_OrderId(user.getUserId(), prevOrder.get().getOrderId());
        }
        if(lectureList.size()>0){ //모두 삭제하지 않았을 경우 하나라도 삭제 안하는 강의가 있을 때
            studentRepository.deleteByUser_UserIdAndOrder_OrderId(user.getUserId(), prevOrder.get().getOrderId());
            //주문을 부분취소로 표시
            prevOrder.ifPresent(order -> order.setOrderStatus(OrderStatus.PARTIAL_CANCEL));
            //구매내역도 invalid 로 표시
            prevPurchase.ifPresent(purchase -> purchase.setValidPurchase(false));

            //주문 다시 생성
            Order newOrder = new Order();
            newOrder.setUser(user);
            newOrder.setPayment(prevOrder.get().getPayment());
            if(prevOrder.get().getPayment() == Payment.BANK_ACCOUNT){
                newOrder.setBank(prevOrder.get().getBank());
                newOrder.setAccount(prevOrder.get().getAccount());
            }
            orderRepository.save(newOrder);

            List<OrderLecture> orderLectureList = newOrder.getOrderLectures();
            // 이전것에서 해당 lectureId만 뺀 lectureList 다시 저장함
            for(Long lectureId : lectureList){
                OrderLecture newOrderLecture = new OrderLecture();
                Optional<Lecture> lecture = lectureRepository.findById(lectureId);
                newOrderLecture.setLecture(lecture.get());
                newOrderLecture.setOrder(newOrder);
                orderLectureList.add(orderLectureRepository.save(newOrderLecture));
            }

            newOrder.setOriginalPrice(orderRepository.findSumOrderByOrderId(newOrder.getOrderId()));
            orderRepository.save(newOrder);


            // 구매 다시 생성
            Purchase purchase = new Purchase();
            newOrder.setOrderStatus(OrderStatus.COMPLETE);//해당 orderId의 주문에는 status를 complete로 변경하기
            purchase.setOrder(newOrder);
            purchase.setPurchasePrice(newOrder.getOriginalPrice());
            purchase.setValidPurchase(true);
            purchaseRepository.save(purchase);

            for(OrderLecture orderLecture : newOrder.getOrderLectures()){
                Student newStudent = new Student();
                newStudent.setUser(user);
                Optional<Lecture> newLecture = lectureRepository.findById(orderLecture.getLecture().getLectureId());
                newStudent.setLecture(newLecture.get());
                newStudent.setOrder(newOrder);
                studentRepository.save(newStudent);
            }
        }


    }

    @Override
    @Transactional
    public void cancelAllPurchases(Long purchaseId){
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        Optional<Purchase> purchase = purchaseRepository.findById(purchaseId);
        if(purchase.isPresent() && purchase.get().isValidPurchase()){
            // 구매 내역을 유효하지 않다고 표시
            purchase.get().setValidPurchase(false);
            //구매내역의 주문번호를 찾아서 cancel
            Order order = purchase.get().getOrder();
            order.setOrderStatus(OrderStatus.CANCEL);
            // student에서도 다 취소
            studentRepository.deleteByUser_UserIdAndOrder_OrderId(user.getUserId(), order.getOrderId());
        }
        else{
            throw new BadRequestArgumentException("해당 구매내역은 수강 취소 불가능합니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
        }
    }

    //전체 구매내역 조회
    @Override
    public List<ResponsePurchaseDto> getAllPurchasesByUserId(){
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        List<Purchase> purchases = purchaseRepository.findByOrder_User_UserIdOrderByPurchaseDateDesc(user.getUserId());

        return purchases.stream().map(ResponsePurchaseDto::new).collect(Collectors.toList());
    }

    // 특정 구매내역 조회
    @Override
    public ResponsePurchaseDto getPurchaseByUserIdAndPurchaseId(Long purchaseId) {

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 구매내역입니다.", ErrorCode.ID_NOT_EXIST));

        return new ResponsePurchaseDto(purchase);
    }

    //전체 구매내역 조회
    @Override
    public List<ResponsePurchaseByAdminDto> getAllPurchasesByUserIdByAdmin(){
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        List<Purchase> purchases = purchaseRepository.findByOrder_User_UserIdOrderByPurchaseDateDesc(user.getUserId());

        return purchases.stream().map(ResponsePurchaseByAdminDto::new).collect(Collectors.toList());
    }

    //특정 사용자의 구매내역 확인(관리자)
    public List<ResponsePurchaseByAdminDto> getPurchaseByUserIdByAdmin(Long userId){
        List<Purchase> purchases = purchaseRepository.findByOrder_User_UserIdOrderByPurchaseDateDesc(userId);
        return purchases.stream().map(ResponsePurchaseByAdminDto::new).collect(Collectors.toList());
    }
}