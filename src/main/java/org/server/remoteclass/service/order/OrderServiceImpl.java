package org.server.remoteclass.service.order;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.OrderStatus;

import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.dto.order.*;
import org.server.remoteclass.entity.*;
import org.server.remoteclass.exception.ForbiddenException;

import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.*;
import org.server.remoteclass.service.order.OrderService;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final OrderRepository orderRepository;
    private final OrderLectureRepository orderLectureRepository;
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,
                            OrderRepository orderRepository, OrderLectureRepository orderLectureRepository,
                            CouponRepository couponRepository, IssuedCouponRepository issuedCouponRepository, BeanConfiguration beanConfiguration){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.orderRepository = orderRepository;
        this.orderLectureRepository = orderLectureRepository;
        this.couponRepository = couponRepository;
        this.issuedCouponRepository = issuedCouponRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @Override
    @Transactional
    public Long createOrder(RequestOrderDto requestOrderDto) throws IdNotExistException{
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setPayment(requestOrderDto.getPayment());
        if(requestOrderDto.getPayment() == Payment.BANK_ACCOUNT){
            order.setBank(requestOrderDto.getBank());
            order.setAccount(requestOrderDto.getAccount());
        }
//        log.info("couponId: " + requestOrderDto.getCouponId());
        IssuedCoupon issuedCoupon = issuedCouponRepository.findByIssuedCouponId(requestOrderDto.getIssuedCouponId());
//        if(coupon.isCouponValid()) { //쿠폰이 유효하면
        order.setIssuedCoupon(issuedCoupon);
//        }
//        else{
//                order.setCoupon(null);
//        }
        orderRepository.save(order);

        List<OrderLecture> orderLectureList = order.getOrderLectures();
        for(RequestOrderLectureDto requestOrderLectureDto : requestOrderDto.getOrderLectures()) {
            OrderLecture orderLecture = new OrderLecture();
            Lecture lecture = lectureRepository.findById(requestOrderLectureDto.getLectureId()).orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));
            orderLecture.setLecture(lecture);
            orderLecture.setOrder(order);
            orderLectureList.add(orderLectureRepository.save(orderLecture));
        }
        return order.getOrderId();
    }

    //    주문 취소
    @Override
    @Transactional
    public void cancelOrder(Long orderId) throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        if(user.getUserId() != order.getUser().getUserId()){
            throw new ForbiddenException("취소 권한이 없습니다", ResultCode.FORBIDDEN);
        }
        order.setOrderStatus(OrderStatus.CANCEL);
    }

    //사용자 본인것만 조회
    @Override
    public List<ResponseOrderDto> getMyOrdersByUserId() throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        List<Order> orders = orderRepository.findByUser_UserIdOrderByOrderDateDesc(user.getUserId());

        return orders.stream().map(order -> new ResponseOrderDto(order)).collect(Collectors.toList());
    }

    //관리자 전체 조회
    @Override
    public List<ResponseOrderByAdminDto> getAllOrdersByAdmin() throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
//        if(user.getAuthority() == Authority.ROLE_ADMIN){
        List<Order> orders = orderRepository.findByOrderByOrderDateDesc();

        return orders.stream().map(order -> new ResponseOrderByAdminDto(order)).collect(Collectors.toList());
//        }
//        else{
//            throw new ForbiddenException("접근 권한 없습니다", ResultCode.FORBIDDEN);
//        }

    }

    //관리자가 사용자별로 조회
    @Override
    public List<ResponseOrderByAdminDto> getOrderByUserIdByAdmin(Long userId) throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

//        if(user.getAuthority() == Authority.ROLE_ADMIN){
        List<Order> orders = orderRepository.findByUser_UserIdOrderByOrderDateDesc(userId);
        return orders.stream().map(order -> new ResponseOrderByAdminDto(order)).collect(Collectors.toList());
//        }
//        else{
//            throw new ForbiddenException("접근 권한 없습니다", ResultCode.FORBIDDEN);
//        }
    }

    //관리자가 사용자별로 조회
    @Override
    @Transactional(readOnly = true)
    public ResponseOrderByAdminDto getOrderByOrderIdByAdmin(Long orderId) throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

//        if(user.getAuthority() == Authority.ROLE_ADMIN){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 주문", ResultCode.ID_NOT_EXIST));
        return new ResponseOrderByAdminDto(order);
//        }
//        else{
//            throw new ForbiddenException("접근 권한 없습니다", ResultCode.FORBIDDEN);
//        }
    }
}
