package org.server.remoteclass.dto.order;

import lombok.*;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class ResponseOrderByAdminDto {

    private Long orderId;
    private Long userId;       //주문하는 회원
    private List<ResponseOrderLectureDto> orderLectures = new ArrayList<>();
    private OrderStatus orderStatus; //주문상태
    private LocalDateTime orderDate;
    private Payment payment; //결제방법
    private String bank;  //입금은행
    private String account;  //예금주
    private Long issuedCouponId;       //적용하는 쿠폰 아이디
    private Integer originalPrice;

    public ResponseOrderByAdminDto(Order order){
        this.orderId = order.getOrderId();
        this.userId = order.getUser().getUserId();
        this.orderStatus = order.getOrderStatus();
        this.orderLectures = order.getOrderLectures().stream().map(ResponseOrderLectureDto::new).collect(Collectors.toList());
        this.orderDate = order.getOrderDate();
        this.payment = order.getPayment();
        this.bank = order.getBank();
        this.account = order.getAccount();
        if(order.getIssuedCoupon() == null){
            this.issuedCouponId = null;
        }
        else {
            this.issuedCouponId = order.getIssuedCoupon().getIssuedCouponId();
        }
        this.originalPrice = order.getOriginalPrice();
    }

}
