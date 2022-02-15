package org.server.remoteclass.dto.order;

import lombok.*;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.OrderLecture;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@Builder
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class OrderDto {
    private Long orderId;

    @NotNull
    private Long userId;
    @NotNull
    private List<OrderLecture> orderLectures = new ArrayList<>();
    private OrderStatus orderStatus; //주문상태
    private LocalDateTime orderDate;
    private Long couponId;       //적용하는 쿠폰 아이디
    @NotNull
    private Payment payment; //결제방법
    private String bank;  //입금은행
    private String account;  //예금주

}
