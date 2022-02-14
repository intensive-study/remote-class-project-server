package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.dto.purchase.PurchaseDto;
import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;
import org.server.remoteclass.entity.Purchase;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.*;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService{

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private PurchaseRepository purchaseRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void OrderServiceImpl(UserRepository userRepository,
                                 OrderRepository orderRepository,
                                 PurchaseRepository purchaseRepository,
                                 BeanConfiguration beanConfiguration){
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.purchaseRepository = purchaseRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @Override
    public PurchaseDto createPurchase(RequestPurchaseDto requestPurchaseDto) throws IdNotExistException {
        //orderId를 입력하면 order의 리스트들의 가격을 더해서 정상가, 할인가 계산하고
        //결제완료일은 현재시점으로
        Order order = orderRepository.findById(requestPurchaseDto.getOrderId())
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        Purchase purchase = modelMapper.map(requestPurchaseDto, Purchase.class);
        purchase.setPurchaseDate(LocalDateTime.now());
        //해당 orderId의 주문에는 status를 complete로 바꾸기
        order.setOrderStatus(OrderStatus.COMPLETE);
        purchase.setOrder(order);
        purchase.setOriginalPrice(calculateOrder(order));
        purchase.setDiscountPrice(discountOrder(order));

        return PurchaseDto.from(purchaseRepository.save(purchase));
    }

    @Override
    public Integer calculateOrder(Order order) {
        Integer totalPrice = 0;
        List<OrderLecture> orderLectureList = order.getOrderLectures();
        for(OrderLecture orderLecture : orderLectureList){
            totalPrice += orderLecture.getLecture().getPrice();
        }
        return totalPrice;
    }

    @Override
    public Integer discountOrder(Order order) {
        Integer totalPrice = calculateOrder(order);
        // order를 통해서 쿠폰 찾아내고
        //쿠폰 유효여부 확인하고
        //정률,정액 쿠폰에 따라서 계산
//        if(정률) {
//            totalPrice *= (1 - discountRate);
//        }
//        if(정액) {
//            totalPrice -= (discountPrice);
//        }

        return totalPrice;
    }

}
