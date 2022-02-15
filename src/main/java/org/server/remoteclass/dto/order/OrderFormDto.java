package org.server.remoteclass.dto.order;

import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.OrderLecture;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderFormDto {
//    private Long orderId;
    private Long userId;       //주문하는 회원
    private List<OrderLecture> orderLectures;
    private OrderStatus orderStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime orderDate;
    //    private Long couponId;       //적용하는 쿠폰 아이디
//    private String bank;
//    private String account;
    private Payment payment;
}
