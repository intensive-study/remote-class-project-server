package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.Authority;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.dto.OrderDto;
import org.server.remoteclass.dto.OrderFormDto;
import org.server.remoteclass.dto.OrderLectureDto;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.*;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    //주문 신청
    @Override
    public OrderDto createOrder(OrderFormDto orderFormDto, List<OrderLecture> orderLectures) throws IdNotExistException{
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        List<OrderLecture> orderLectureList = new ArrayList<>();
        for(OrderLecture orderLecture : orderLectures) {
            orderLectureList.add(orderLecture);
        }


        Order order = modelMapper.map(orderFormDto, Order.class);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDate.now());
        order.setPayment(orderFormDto.getPayment());
        order.setOrderLectures(orderLectures);

        return OrderDto.from(orderRepository.save(order));
    }

    //주문 취소
    @Override
    public OrderDto cancelOrder(Long orderId) throws IdNotExistException{
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        Order order = orderRepository.findById(orderId).orElseThrow(null);
        // 해당 주문번호가 존재하면 주문번호의 userId와 현재 회원 아이디와 일치여부 확인
        if(user.getUserId() == order.getUser().getUserId()){
            order.setOrderStatus(OrderStatus.CANCEL);
        }

        return OrderDto.from(order);
    }


    //주문 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserId() throws IdNotExistException {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        List<Order> orders;
        //관리자 권한일때는 모든 주문내역을 조회 가능
        if(user.getAuthority() == Authority.ROLE_ADMIN){
            orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        }
        // 사용자 권한일때는 현재 회원의 주문내역을 조회 가능
        else{
            orders = orderRepository.findByUserId(user.getUserId());
        }
        return orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
    }

    //주문의 가격합
    @Override
    @Transactional(readOnly = true)
    public Double getSumOrdersByOrderId() throws IdNotExistException{
        return null;
    }
}
