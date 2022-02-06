package org.server.remoteclass.dto;

import lombok.*;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class OrderDto {
    private Long orderId;
    private Long userId;       //주문하는 회원
    private List<OrderLecture> orderLectures;
    private OrderStatus orderStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    //    private Long couponId;       //적용하는 쿠폰 아이디
//    private String bank;
//    private String account;
    private Payment payment;


    public OrderDto(){

    }
    public OrderDto(Order order){
        this.orderId = order.getOrderId();
        this.userId = order.getUser().getUserId();
        this.orderLectures = order.getOrderLectures();
        this.orderStatus = order.getOrderStatus();
        this.orderDate = LocalDate.from(order.getOrderDate());
        this.payment = order.getPayment();
    }

    public static OrderDto from(Order order){
        if(order == null) return null;
        return OrderDto.builder()
                .userId(order.getUser().getUserId())
                .orderLectures(order.getOrderLectures())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .payment(order.getPayment())
                .build();
    }


}
