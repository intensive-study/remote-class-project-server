package org.server.remoteclass.dto;


import lombok.*;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ResponseOrderDto {

    private Long orderId;
    private Long userId;
    private List<ResponseOrderLectureDto> orderLectures = new ArrayList<>();
    private OrderStatus orderStatus; //주문상태
    private Payment payment; //결제방법
    private String bank;  //입금은행
    private String account;  //예금주
    private Long couponId;       //적용하는 쿠폰 아이디

    public static ResponseOrderDto from(Order order){
        if(order == null) return null;
        return ResponseOrderDto.builder()
                .userId(order.getUser().getUserId())
                .orderId(order.getOrderId())
                .orderLectures(order.getOrderLectures().stream()
                        .map(ResponseOrderLectureDto::new)
                        .collect(Collectors.toList())
                )
                .orderStatus(order.getOrderStatus())
                .payment(order.getPayment())
                .bank(order.getBank())
                .account(order.getAccount())
                .couponId(order.getCoupon().getCouponId())
                .build();
    }

}
