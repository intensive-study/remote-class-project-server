package org.server.remoteclass.dto.coupon;

import lombok.*;
import org.server.remoteclass.entity.Coupon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCouponDto {

    private Long couponId;
    private String couponCode;
    private boolean couponValid;
    private int couponValidDays;
    private LocalDateTime createdDate;
    private List<ResponseIssuedCouponDto> issuedCouponList = new ArrayList<>();

    public static ResponseCouponDto from(Coupon coupon){
        if(coupon == null) return null;
        return ResponseCouponDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .couponValid(coupon.isCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCreatedDate())
                .issuedCouponList(coupon.getIssuedCouponList().stream()
                        .map(issuedCoupon ->
                                ResponseIssuedCouponDto.from(issuedCoupon)
                        ).collect(Collectors.toList()))
                .build();
    }
}
