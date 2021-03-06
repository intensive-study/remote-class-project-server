package org.server.remoteclass.dto.order;

import lombok.*;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class RequestOrderDto {

    //주문 신청할때 입력하는 dto
    @NotEmpty
    private List<RequestOrderLectureDto> orderLectures = new ArrayList<>();

    private Payment payment; //결제방법

    private String bank;  //입금은행
    private String account;  //예금주

    private Long issuedCouponId;       //적용하는 쿠폰 아이디

    public static RequestOrderDto from(Order order){
        if(order == null) return null;
        return RequestOrderDto.builder()
                .orderLectures(order.getOrderLectures().stream()
                        .map(RequestOrderLectureDto::new)
                        .collect(Collectors.toList())
                )
                .payment(order.getPayment())
                .bank(order.getBank())
                .account(order.getAccount())
                .issuedCouponId(order.getIssuedCoupon().getIssuedCouponId())
                .build();
    }

}
