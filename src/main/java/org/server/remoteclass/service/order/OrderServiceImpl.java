package org.server.remoteclass.service.order;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.OrderStatus;

import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.dto.order.*;
import org.server.remoteclass.entity.*;
import org.server.remoteclass.exception.BadRequestArgumentException;
import org.server.remoteclass.exception.ForbiddenException;

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
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final OrderRepository orderRepository;
    private final OrderLectureRepository orderLectureRepository;
    private final FixDiscountCouponRepository fixDiscountCouponRepository;
    private final RateDiscountCouponRepository rateDiscountCouponRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final AccessVerification accessVerification;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,
                            OrderRepository orderRepository, OrderLectureRepository orderLectureRepository,
                            FixDiscountCouponRepository fixDiscountCouponRepository, RateDiscountCouponRepository rateDiscountCouponRepository,IssuedCouponRepository issuedCouponRepository,
                            BeanConfiguration beanConfiguration, AccessVerification accessVerification){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.orderRepository = orderRepository;
        this.orderLectureRepository = orderLectureRepository;
        this.fixDiscountCouponRepository = fixDiscountCouponRepository;
        this.rateDiscountCouponRepository = rateDiscountCouponRepository;
        this.issuedCouponRepository = issuedCouponRepository;
        this.modelMapper = beanConfiguration.modelMapper();
        this.accessVerification = accessVerification;
    }

    @Override
    @Transactional
    public void createOrder(RequestOrderDto requestOrderDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPayment(requestOrderDto.getPayment());
        if(requestOrderDto.getPayment() == Payment.BANK_ACCOUNT){
            order.setBank(requestOrderDto.getBank());
            order.setAccount(requestOrderDto.getAccount());
        }
        IssuedCoupon issuedCoupon = issuedCouponRepository.findByIssuedCouponId(requestOrderDto.getIssuedCouponId());
        if(requestOrderDto.getIssuedCouponId() == null){ //쿠폰값 입력 안했을때
            order.setIssuedCoupon(null);
        }
        else{  //쿠폰값 입력했을때
            if(requestOrderDto.getIssuedCouponId() != null){
                if(issuedCoupon==null){  //없는 쿠폰 입력했을 때
                    throw new IdNotExistException("존재하지 않는 쿠폰입니다", ErrorCode.ID_NOT_EXIST);
                }
                // 이미 사용한 쿠폰 입력했을 때 or 유효하지 않는 쿠폰 입력했을 때
                if(issuedCoupon.isCouponUsed() || LocalDateTime.now().isAfter(issuedCoupon.getCouponValidDate())){
                    throw new BadRequestArgumentException("이미 사용했거나 유효하지 않은 쿠폰입니다", ErrorCode.BAD_REQUEST_ARGUMENT);
                }
                order.setIssuedCoupon(issuedCoupon);
                issuedCoupon.setCouponUsed(true);
            }
        orderRepository.save(order);

        List<OrderLecture> orderLectureList = order.getOrderLectures();
        for(RequestOrderLectureDto requestOrderLectureDto : requestOrderDto.getOrderLectures()) {
            OrderLecture orderLecture = new OrderLecture();
            Lecture lecture = lectureRepository.findById(requestOrderLectureDto.getLectureId()).orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ErrorCode.ID_NOT_EXIST));
            orderLecture.setLecture(lecture);
            orderLecture.setOrder(order);
            orderLectureList.add(orderLectureRepository.save(orderLecture));
        }
        order.setOriginalPrice(orderRepository.findSumOrderByOrderId(order.getOrderId()));

        Integer price = order.getOriginalPrice();
        if(fixDiscountCouponRepository.existsByCouponId(issuedCoupon.getCoupon().getCouponId())){
            Optional<FixDiscountCoupon> fixDiscountCoupon = fixDiscountCouponRepository.findByCouponId(issuedCoupon.getCoupon().getCouponId());
            price -= fixDiscountCoupon.get().getDiscountPrice();
        }
        else if(rateDiscountCouponRepository.existsByCouponId(issuedCoupon.getCoupon().getCouponId())){
            Optional<RateDiscountCoupon> rateDiscountCoupon = rateDiscountCouponRepository.findByCouponId(issuedCoupon.getCoupon().getCouponId());
            price *= (1-rateDiscountCoupon.get().getDiscountRate());
        }
            order.setSalePrice(price);
        }
        orderRepository.save(order);

    }

    //    주문 취소
    @Override
    @Transactional
    public void cancelOrder(Long orderId) throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IdNotExistException("해당 주문이 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));
        if(user.getUserId() != order.getUser().getUserId()){
            throw new ForbiddenException("주문 취소 권한이 없습니다", ErrorCode.FORBIDDEN);
        }
        if(order.getOrderStatus() == OrderStatus.PENDING) {
            order.setOrderStatus(OrderStatus.CANCEL);
        }
        else{
            throw new BadRequestArgumentException("취소 가능한 상태가 아닙니다", ErrorCode.BAD_REQUEST_ARGUMENT);
        }
    }

    //사용자 본인것만 조회
    @Override
    public List<ResponseOrderDto> getMyOrdersByUserId() throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        List<Order> orders = orderRepository.findByUser_UserIdOrderByOrderDateDesc(user.getUserId());
        return orders.stream().map(ResponseOrderDto::new).collect(Collectors.toList());
    }

    //관리자 전체 조회
    @Override
    public List<ResponseOrderByAdminDto> getAllOrdersByAdmin() throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        List<Order> orders = orderRepository.findByOrderByOrderDateDesc();

        return orders.stream().map(ResponseOrderByAdminDto::new).collect(Collectors.toList());
    }

    //관리자가 사용자별로 조회
    @Override
    public List<ResponseOrderByAdminDto> getOrderByUserIdByAdmin(Long userId) throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        List<Order> orders = orderRepository.findByUser_UserIdOrderByOrderDateDesc(userId);

        return orders.stream().map(ResponseOrderByAdminDto::new).collect(Collectors.toList());
    }

    //관리자가 주문번호별로 조회
    @Override
    @Transactional(readOnly = true)
    public ResponseOrderByAdminDto getOrderByOrderIdByAdmin(Long orderId) throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IdNotExistException("해당 주문이 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));

        return new ResponseOrderByAdminDto(order);
    }
}
