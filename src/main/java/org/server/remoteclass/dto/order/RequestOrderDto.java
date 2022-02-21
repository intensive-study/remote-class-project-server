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
    @NotEmpty // 리스트라 @NotEmpty 해도 되는지 확실치 않습니다.
    private List<RequestOrderLectureDto> orderLectures = new ArrayList<>();
    @NotEmpty
    private Payment payment; //결제방법
    @NotEmpty
    private String bank;  //입금은행
    @NotEmpty
    private String account;  //예금주
    @NotEmpty
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
