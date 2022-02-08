package org.server.remoteclass.dto;

import lombok.*;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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
    private LocalDateTime orderDate;
    //    private Long couponId;       //적용하는 쿠폰 아이디
//    private String bank;
//    private String account;
    private Payment payment;


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
