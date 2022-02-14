package org.server.remoteclass.dto.order;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.*;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class RequestOrderDto {

    private List<ResponseOrderLectureDto> orderLectures = new ArrayList<>();
//    private OrderStatus orderStatus; //주문상태
    private Payment payment; //결제방법
    private String bank;  //입금은행
    private String account;  //예금주
    private Long couponId;       //적용하는 쿠폰 아이디

    public static RequestOrderDto from(Order order){
        if(order == null) return null;
        return RequestOrderDto.builder()
                .orderLectures(order.getOrderLectures().stream()
                        .map(ResponseOrderLectureDto::new)
                        .collect(Collectors.toList())
                )
//                .orderStatus(order.getOrderStatus())
                .payment(order.getPayment())
                .bank(order.getBank())
                .account(order.getAccount())
                .couponId(order.getCoupon().getCouponId())
                .build();
    }

}
