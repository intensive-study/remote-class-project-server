package org.server.remoteclass.dto.order;

import lombok.*;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.Order;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ResponseOrderDto {

    //회원이 주문 조회할때 사용하는 dto

    private Long orderId;
    @NotNull
    private List<ResponseOrderLectureDto> orderLectures = new ArrayList<>();
    private OrderStatus orderStatus; //주문상태
    private LocalDateTime orderDate;
    @NotNull
    private Payment payment; //결제방법
    private String bank;  //입금은행
    private String account;  //예금주
    private Long issuedCouponId;       //적용하는 쿠폰 아이디
    private String issuedCouponName;
    private Integer originalPrice;
    private Integer salePrice;

    public ResponseOrderDto(Order order){
        this.orderId = order.getOrderId();
        this.orderStatus = order.getOrderStatus();
        this.orderLectures = order.getOrderLectures().stream().map(ResponseOrderLectureDto::new).collect(Collectors.toList());
        this.orderDate = order.getOrderDate();
        this.payment = order.getPayment();
        this.bank = order.getBank();
        this.account = order.getAccount();
        if(order.getIssuedCoupon() == null){
            this.issuedCouponId = null;
            this.salePrice = null;
        }
        else {
            this.issuedCouponId = order.getIssuedCoupon().getIssuedCouponId();
            this.issuedCouponName = order.getIssuedCoupon().getCoupon().getTitle();
            this.salePrice = order.getSalePrice();
        }
        this.originalPrice = order.getOriginalPrice();
    }

}
