package org.server.remoteclass.dto;

import lombok.*;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class OrderHistoryDto {


    private Long orderId;
    private Long userId;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private Long couponId;       //적용하는 쿠폰 아이디
    private String bank;
    private String account;
    private Payment payment;
    private List<OrderLectureDto> orderLectureDtoList = new ArrayList<>();

    public OrderHistoryDto(Order order){
        this.orderId = order.getOrderId();
        this.userId = order.getUser().getUserId();
        this.orderStatus = order.getOrderStatus();
        this.orderDate = order.getOrderDate();
        this.couponId = order.getCoupon().getCouponId();
        this.payment = order.getPayment();
        this.bank = order.getBank();
        this.account = order.getAccount();
    }
}
