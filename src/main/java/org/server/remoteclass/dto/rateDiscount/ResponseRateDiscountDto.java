package org.server.remoteclass.dto.rateDiscount;

import lombok.*;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.dto.issuedcoupon.ResponseIssuedCouponDto;
import org.server.remoteclass.entity.Coupon;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRateDiscountDto {

    private Long couponId;
    private String couponCode;
    private boolean couponValid;
    private int couponValidDays;
    private LocalDateTime createdDate;
    private int rateDiscount;

    public static ResponseRateDiscountDto from(Coupon coupon){
        if(coupon == null) return null;
        return ResponseRateDiscountDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .couponValid(coupon.isCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCreatedDate())
                .rateDiscount(coupon.getDiscountRate())
                .build();
    }
}
