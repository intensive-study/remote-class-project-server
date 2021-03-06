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
import org.server.remoteclass.service.purchase.PurchaseService;
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
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final OrderRepository orderRepository;
    private final OrderLectureRepository orderLectureRepository;
    private final CartRepository cartRepository;
    private final FixDiscountCouponRepository fixDiscountCouponRepository;
    private final RateDiscountCouponRepository rateDiscountCouponRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final AccessVerification accessVerification;
    private final ModelMapper modelMapper;
    private final PurchaseService purchaseService;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository,
                            LectureRepository lectureRepository,
                            OrderRepository orderRepository,
                            OrderLectureRepository orderLectureRepository,
                            CartRepository cartRepository,
                            FixDiscountCouponRepository fixDiscountCouponRepository,
                            RateDiscountCouponRepository rateDiscountCouponRepository,
                            IssuedCouponRepository issuedCouponRepository,
                            BeanConfiguration beanConfiguration,
                            AccessVerification accessVerification,
                            PurchaseService purchaseService){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.orderRepository = orderRepository;
        this.orderLectureRepository = orderLectureRepository;
        this.cartRepository = cartRepository;
        this.fixDiscountCouponRepository = fixDiscountCouponRepository;
        this.rateDiscountCouponRepository = rateDiscountCouponRepository;
        this.issuedCouponRepository = issuedCouponRepository;
        this.modelMapper = beanConfiguration.modelMapper();
        this.accessVerification = accessVerification;
        this.purchaseService = purchaseService;
    }


    @Override
    @Transactional
    public void createOrder(RequestOrderDto requestOrderDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ????????? ????????????.", ErrorCode.ID_NOT_EXIST));
        Order order = new Order();
        order.setUser(user);
        order.setPayment(requestOrderDto.getPayment());
        if(requestOrderDto.getPayment() == Payment.BANK_ACCOUNT){
            order.setBank(requestOrderDto.getBank());
            order.setAccount(requestOrderDto.getAccount());
        }
        orderRepository.save(order);

        List<OrderLecture> orderLectureList = order.getOrderLectures();
        for(RequestOrderLectureDto requestOrderLectureDto : requestOrderDto.getOrderLectures()) {
            OrderLecture orderLecture = new OrderLecture();
            Lecture lecture = lectureRepository.findById(requestOrderLectureDto.getLectureId()).orElseThrow(() -> new IdNotExistException("???????????? ?????? ??????", ErrorCode.ID_NOT_EXIST));
            orderLecture.setLecture(lecture);
            orderLecture.setOrder(order);
            orderLectureList.add(orderLectureRepository.save(orderLecture));
        }
        order.setOriginalPrice(orderRepository.findSumOrderByOrderId(order.getOrderId()));

        Integer price = order.getOriginalPrice();
        if(requestOrderDto.getIssuedCouponId() != null) {
            IssuedCoupon issuedCoupon = issuedCouponRepository.findByIssuedCouponId(requestOrderDto.getIssuedCouponId());
            if (issuedCoupon == null) {  //?????? ?????? ???????????? ???
                throw new IdNotExistException("???????????? ?????? ???????????????", ErrorCode.ID_NOT_EXIST);
            }
            // ?????? ????????? ?????? ???????????? ??? or ???????????? ?????? ?????? ???????????? ???
            if (issuedCoupon.isCouponUsed() || LocalDateTime.now().isAfter(issuedCoupon.getCouponValidDate())) {
                throw new BadRequestArgumentException("?????? ??????????????? ???????????? ?????? ???????????????", ErrorCode.BAD_REQUEST_ARGUMENT);
            }
            order.setIssuedCoupon(issuedCoupon);
            issuedCoupon.setCouponUsed(true);

            if(fixDiscountCouponRepository.existsByCouponId(issuedCoupon.getCoupon().getCouponId())){
                Optional<FixDiscountCoupon> fixDiscountCoupon = fixDiscountCouponRepository.findByCouponId(issuedCoupon.getCoupon().getCouponId());
                if(fixDiscountCoupon.get().getDiscountPrice() > order.getOriginalPrice()) throw new BadRequestArgumentException("????????? ????????? ??? ????????????.", ErrorCode.BAD_REQUEST_ARGUMENT);
                price -= fixDiscountCoupon.get().getDiscountPrice();
            }
            else if(rateDiscountCouponRepository.existsByCouponId(issuedCoupon.getCoupon().getCouponId())){
                Optional<RateDiscountCoupon> rateDiscountCoupon = rateDiscountCouponRepository.findByCouponId(issuedCoupon.getCoupon().getCouponId());
                double ratePrice = ((100-rateDiscountCoupon.get().getDiscountRate())*0.01);
                price = (int)(price * ratePrice);
            }
        }

        order.setSalePrice(price);
        orderRepository.save(order);

    }

    @Override
    @Transactional
    public void createOrderFromCart(RequestOrderDto requestOrderDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ????????? ????????????.", ErrorCode.ID_NOT_EXIST));
        Order order = new Order();
        order.setUser(user);
        order.setPayment(requestOrderDto.getPayment());
        if(requestOrderDto.getPayment() == Payment.BANK_ACCOUNT){
            order.setBank(requestOrderDto.getBank());
            order.setAccount(requestOrderDto.getAccount());
        }

        orderRepository.save(order);

        List<Cart> cartList = new ArrayList<>();
        for(RequestOrderLectureDto requestOrderLectureDto : requestOrderDto.getOrderLectures()){
            Cart cart = cartRepository.findByLecture_LectureIdAndUser_UserId(requestOrderLectureDto.getLectureId(), user.getUserId())
                    .orElseThrow(() -> new BadRequestArgumentException("??????????????? ?????? ???????????????.", ErrorCode.BAD_REQUEST_ARGUMENT));
            cartList.add(cart);
        }

        List<OrderLecture> orderLectureList = order.getOrderLectures();
        //?????? ?????? ???????????? ??????????????? ????????????.
        for(Cart cart : cartList){
            OrderLecture orderLecture = new OrderLecture();
            orderLecture.setLecture(cart.getLecture());
            orderLecture.setOrder(order);
            orderLectureList.add(orderLectureRepository.save(orderLecture));
            cartRepository.deleteByLecture_LectureIdAndUser_UserId(cart.getLecture().getLectureId(), user.getUserId());
        }
        order.setOriginalPrice(orderRepository.findSumOrderByOrderId(order.getOrderId()));

        Integer price = order.getOriginalPrice();
        if(requestOrderDto.getIssuedCouponId() != null) {
            IssuedCoupon issuedCoupon = issuedCouponRepository.findByIssuedCouponId(requestOrderDto.getIssuedCouponId());
            if (issuedCoupon == null) {  //?????? ?????? ???????????? ???
                throw new IdNotExistException("???????????? ?????? ???????????????", ErrorCode.ID_NOT_EXIST);
            }
            // ?????? ????????? ?????? ???????????? ??? or ???????????? ?????? ?????? ???????????? ???
            if (issuedCoupon.getCouponUsed() || LocalDateTime.now().isAfter(issuedCoupon.getCouponValidDate())) {
                throw new BadRequestArgumentException("?????? ??????????????? ???????????? ?????? ???????????????", ErrorCode.BAD_REQUEST_ARGUMENT);
            }
            order.setIssuedCoupon(issuedCoupon);
            issuedCoupon.setCouponUsed(true);

            if(fixDiscountCouponRepository.existsByCouponId(issuedCoupon.getCoupon().getCouponId())){
                Optional<FixDiscountCoupon> fixDiscountCoupon = fixDiscountCouponRepository.findByCouponId(issuedCoupon.getCoupon().getCouponId());
                if(fixDiscountCoupon.get().getDiscountPrice() > order.getOriginalPrice()) throw new BadRequestArgumentException("????????? ????????? ??? ????????????.", ErrorCode.BAD_REQUEST_ARGUMENT);
                price -= fixDiscountCoupon.get().getDiscountPrice();
            }
            else if(rateDiscountCouponRepository.existsByCouponId(issuedCoupon.getCoupon().getCouponId())){
                Optional<RateDiscountCoupon> rateDiscountCoupon = rateDiscountCouponRepository.findByCouponId(issuedCoupon.getCoupon().getCouponId());
                double ratePrice = ((100-rateDiscountCoupon.get().getDiscountRate())*0.01);
                price = (int)(price * ratePrice);
            }
        }

        order.setSalePrice(price);
        orderRepository.save(order);

    }

    //    ?????? ??????
    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ????????? ????????????.", ErrorCode.ID_NOT_EXIST));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ???????????? ????????????.", ErrorCode.ID_NOT_EXIST));
        if(user.getUserId() != order.getUser().getUserId()){
            throw new ForbiddenException("?????? ?????? ????????? ????????????", ErrorCode.FORBIDDEN);
        }
        if(order.getOrderStatus() == OrderStatus.PENDING) {
            order.setOrderStatus(OrderStatus.CANCEL);
        }
        else{
            throw new BadRequestArgumentException("?????? ????????? ????????? ????????????", ErrorCode.BAD_REQUEST_ARGUMENT);
        }
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ????????? ????????????.", ErrorCode.ID_NOT_EXIST));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ???????????? ????????????.", ErrorCode.ID_NOT_EXIST));
        if(user.getUserId() != order.getUser().getUserId()){
            throw new ForbiddenException("?????? ?????? ????????? ????????????", ErrorCode.FORBIDDEN);
        }
        if(order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BadRequestArgumentException("????????? ??? ?????? ???????????????.", ErrorCode.BAD_REQUEST_ARGUMENT);
        }
        order.setOrderStatus(OrderStatus.COMPLETE);
        purchaseService.createPurchase(orderId);

    }

    //????????? ???????????? ??????
    @Override
    public List<ResponseOrderDto> getMyOrdersByUserId() {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ????????? ????????????.", ErrorCode.ID_NOT_EXIST));
        List<Order> orders = orderRepository.findByUser_UserIdOrderByOrderDateDesc(user.getUserId());
        return orders.stream().map(ResponseOrderDto::new).collect(Collectors.toList());
    }

    //????????? ?????? ??????
    @Override
    public List<ResponseOrderByAdminDto> getAllOrdersByAdmin() {

        List<Order> orders = orderRepository.findByOrderByOrderDateDesc();

        return orders.stream().map(ResponseOrderByAdminDto::new).collect(Collectors.toList());
    }

    //???????????? ??????????????? ??????
    @Override
    public List<ResponseOrderByAdminDto> getOrderByUserIdByAdmin(Long userId) {

        List<Order> orders = orderRepository.findByUser_UserIdOrderByOrderDateDesc(userId);

        return orders.stream().map(ResponseOrderByAdminDto::new).collect(Collectors.toList());
    }

    //???????????? ?????????????????? ??????
    @Override
    public ResponseOrderByAdminDto getOrderByOrderIdByAdmin(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ???????????? ????????????.", ErrorCode.ID_NOT_EXIST));

        return new ResponseOrderByAdminDto(order);
    }
}
