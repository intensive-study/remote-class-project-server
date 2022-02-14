package org.server.remoteclass.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class ResponseOrderByAdminDto {

    private Long orderId;
    private Long userId;       //주문하는 회원
    private List<ResponseOrderLectureDto> orderLectures = new ArrayList<>();
    private OrderStatus orderStatus; //주문상태
    private Payment payment; //결제방법
    private String bank;  //입금은행
    private String account;  //예금주
    private Long couponId;       //적용하는 쿠폰 아이디

    public static ResponseOrderByAdminDto from(Order order){
        if(order == null) return null;
        return ResponseOrderByAdminDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
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
