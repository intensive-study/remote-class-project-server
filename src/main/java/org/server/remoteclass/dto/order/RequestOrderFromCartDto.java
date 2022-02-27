package org.server.remoteclass.dto.order;

import lombok.*;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.Order;


@Builder
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class RequestOrderFromCartDto {

    //장바구니에서 주문 신청할때 입력하는 dto
    private Payment payment; //결제방법

    private String bank;  //입금은행
    private String account;  //예금주

    private Long issuedCouponId;       //적용하는 쿠폰 아이디

    public static RequestOrderFromCartDto from(Order order){
        if(order == null) return null;
        return RequestOrderFromCartDto.builder()
                .payment(order.getPayment())
                .bank(order.getBank())
                .account(order.getAccount())
                .issuedCouponId(order.getIssuedCoupon().getIssuedCouponId())
                .build();
    }

}
