package org.server.remoteclass.service;

import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.Authority;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.dto.OrderDto;
import org.server.remoteclass.dto.OrderFormDto;
import org.server.remoteclass.dto.OrderLectureDto;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.*;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final OrderRepository orderRepository;
    private final OrderLectureRepository orderLectureRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,
                              OrderRepository orderRepository, OrderLectureRepository orderLectureRepository,BeanConfiguration beanConfiguration){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.orderRepository = orderRepository;
        this.orderLectureRepository = orderLectureRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @ApiOperation("주문 신청")
    @Override
    public Long createOrder(OrderDto orderDto) throws IdNotExistException{
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        Lecture lecture = lectureRepository.findById(orderDto.getLectureId())
                .orElseThrow(EntityNotFoundException::new);

        List<OrderLecture> orderLectureList = new ArrayList<>();
//        OrderLecture orderLecture = OrderLecture.createOrderLecture(lecture);
        OrderLecture orderLecture = new OrderLecture();
        orderLecture.setLecture(lecture);
        orderLectureList.add(orderLecture);

//        Order order = Order.createOrder(user, orderLectureList);
        Order order = new Order();
        order.setUser(user);
        order.setOrderLectures(orderLectureList);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setPayment(orderDto.getPayment());
        if(orderDto.getPayment() == Payment.BANK_ACCOUNT){
            order.setBank(orderDto.getBank());
            order.setAccount(orderDto.getAccount());
        }
        orderRepository.save(order);

        return order.getOrderId();
    }

    //주문 취소
    @Override
    public void cancelOrder(Long orderId) throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        if(user.getUserId() != order.getUser().getUserId()){
            throw new ForbiddenException("취소 권한이 없습니다", ResultCode.FORBIDDEN);
        }
//        order.cancelOrder();
        order.setOrderStatus(OrderStatus.CANCEL);
        for(OrderLecture orderLecture : order.getOrderLectures()){
            orderLecture.getLecture();
        }
    }
//    public Long createOrderList(List<OrderDto> orderDtoList) throws IdNotExistException {
//
//        User user = SecurityUtil.getCurrentUserEmail()
//                .flatMap(userRepository::findByEmail)
//                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
//
//        List<OrderLecture> orderLectureList = new ArrayList<>();
//
//        for (OrderDto orderDto : orderDtoList) {
//            Lecture lecture = lectureRepository.findById(orderDto.getLectureId())
//                    .orElseThrow(EntityNotFoundException::new);
//
////            OrderLecture orderLecture = OrderLecture.createOrderLecture(lecture);
//            OrderLecture orderLecture = new OrderLecture();
//            orderLecture.setLecture(lecture);
//
//            orderLectureList.add(orderLecture);
//        }
//
////        Order order = Order.createOrder(user, orderLectureList);
//        Order order = new Order();
//        order.setUser(user);
//        for(OrderLecture orderLectureElem : orderLectureList){
////            order.addOrderLecture(orderLectureElem);
//            orderLectureList.add(orderLectureElem);
//            orderLectureElem.setOrder(order);
//        }
//        order.setOrderStatus(OrderStatus.PENDING);
//        order.setOrderDate(LocalDateTime.now());
//        order.setPayment(orderDto.getPayment());
//        if(orderDto.getPayment() == Payment.BANK_ACCOUNT){
//            order.setBank(orderDto.getBank());
//            order.setAccount(orderDto.getAccount());
//        }
//        orderRepository.save(order);
//
//        return order.getOrderId();
//    }

}
