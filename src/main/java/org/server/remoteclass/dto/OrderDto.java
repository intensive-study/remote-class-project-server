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
    // 주문 생성시 입력하는 dto
    private Long lectureId;
    private Payment payment;
    private String bank;
    private String account;
    private Long couponId;
}
