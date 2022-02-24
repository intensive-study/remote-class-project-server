package org.server.remoteclass.service.purchase;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService{

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final OrderRepository orderRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final PurchaseRepository purchaseRepository;
    private final AccessVerification accessVerification;
    private final ModelMapper modelMapper;

    @Autowired
    public PurchaseServiceImpl(UserRepository userRepository,
                               LectureRepository lectureRepository,
                               StudentRepository studentRepository,
                               OrderRepository orderRepository,
                               PurchaseRepository purchaseRepository,
                               IssuedCouponRepository issuedCouponRepository,
                               BeanConfiguration beanConfiguration,
                               AccessVerification accessVerification){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.orderRepository = orderRepository;
        this.issuedCouponRepository = issuedCouponRepository;
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
                    studentRepository.save(student);
                }
            }
        }
        else{
            throw new BadRequestArgumentException("주문 가능한 상태가 아닙니다", ErrorCode.BAD_REQUEST_ARGUMENT);
        }

    }

    //전체 구매내역 조회
    @Override
    public List<ResponsePurchaseDto> getAllPurchaseByUserId(){
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        List<Purchase> purchases = purchaseRepository.findByOrder_User_UserIdOrderByPurchaseDateDesc(user.getUserId());

        return purchases.stream().map(ResponsePurchaseDto::new).collect(Collectors.toList());
    }

    // 특정 구매내역 조회
    @Override
    public ResponsePurchaseDto getPurchaseByUserIdAndPurchaseId(Long purchaseId) {

        Purchase purchase = purchaseRepository.findById(purchaseId).orElse(null);

        return new ResponsePurchaseDto(purchase);
    }
}