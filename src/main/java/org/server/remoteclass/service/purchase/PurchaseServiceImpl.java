package org.server.remoteclass.service.purchase;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
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
    public void cancelPurchase(Long lectureId) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IdNotExistException("해당 강의가 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));

        //수강 신청한 강의이고 시작을 아직 안했을 경우
        if(studentRepository.existsByLecture_LectureIdAndUser_UserId(lectureId, user.getUserId())
                && lecture.getStartDate().isAfter(LocalDateTime.now())){
            //lectureId가 포함된 order
            Student student = studentRepository.findStudentByLecture_LectureId(lectureId);
            Order prevOrder = orderRepository.findById(student.getOrder().getOrderId())
                    .orElseThrow(() -> new IdNotExistException("해당 주문이 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));
            // 주문이 존재하면 해당 주문내역 부분취소로 표시
            prevOrder.setOrderStatus(OrderStatus.PARTIAL_CANCEL);
            // 같은주문에서 신청한 student는 모두 삭제
            studentRepository.deleteByUser_UserIdAndOrder_OrderId(user.getUserId(), prevOrder.getOrderId());
            //해당 구매내역 invalid로 표시
            Purchase prevPurchase = purchaseRepository.findByOrder_OrderId(prevOrder.getOrderId())
                    .orElseThrow(() -> new IdNotExistException("해당 구매내역이 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));
            prevPurchase.setValidPurchase(false);

            //주문 내역 다시 생성
            Order order = new Order();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setPayment(prevOrder.getPayment());
            if(prevOrder.getPayment() == Payment.BANK_ACCOUNT){
                order.setBank(prevOrder.getBank());
                order.setAccount(prevOrder.getAccount());
            }
            orderRepository.save(order);

            List<OrderLecture> orderLectureList = order.getOrderLectures();
            // 이전것에서 해당 lectureId만 빼고 다시 저장함
            for(OrderLecture orderLecture : prevOrder.getOrderLectures()) {
                //해당 lectureId가 아니면 저장
                if(orderLecture.getLecture().getLectureId() != lectureId){
                    OrderLecture newOrderLecture = new OrderLecture();
                    Lecture newLecture = lectureRepository.findById(orderLecture.getLecture().getLectureId())
                            .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ErrorCode.ID_NOT_EXIST));
                    newOrderLecture.setLecture(newLecture);
                    newOrderLecture.setOrder(order);
                    orderLectureList.add(orderLectureRepository.save(newOrderLecture));
                }
            }
            //모두 삭제했을 경우
            if(orderLectureList.isEmpty()){
                orderRepository.delete(order);
                throw new BadRequestArgumentException("더이상 수강 취소할 강의가 없습니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
            }
            order.setOriginalPrice(orderRepository.findSumOrderByOrderId(order.getOrderId()));

            orderRepository.save(order);

            //부분 취소 후에는 다시 구매
            //구매내역 다시 생성
            Purchase purchase = new Purchase();
            order.setOrderStatus(OrderStatus.COMPLETE);//해당 orderId의 주문에는 status를 complete로 변경하기
            purchase.setOrder(order);
            purchase.setPurchasePrice(order.getOriginalPrice());
            purchase.setValidPurchase(true);
            purchaseRepository.save(purchase);
            log.info("새 구매내역 id: " + purchase.getPurchaseId());
            for(OrderLecture orderLecture : order.getOrderLectures()){
                Student newStudent = new Student();
                newStudent.setUser(user);
                Lecture newLecture = lectureRepository.findById(orderLecture.getLecture().getLectureId())
                        .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의입니다.", ErrorCode.ID_NOT_EXIST));
                newStudent.setLecture(newLecture);
                newStudent.setOrder(order);
                studentRepository.save(newStudent);
            }
        }
        else{
            throw new IdNotExistException("취소할 강의가 없습니다.", ErrorCode.ID_NOT_EXIST);
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