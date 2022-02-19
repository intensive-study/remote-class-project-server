package org.server.remoteclass.dto.rateDiscountCoupon;

import lombok.*;
import org.server.remoteclass.entity.RateDiscountCoupon;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRateDiscountCouponDto {

    private Long couponId;
    private String couponCode;
    private boolean couponValid;
    private int couponValidDays;
    private LocalDateTime createdDate;
    private int discountRate;

    public static ResponseRateDiscountCouponDto from(RateDiscountCoupon rateDiscountCoupon){
        if(rateDiscountCoupon == null) return null;
        return ResponseRateDiscountCouponDto.builder()
                .couponId(rateDiscountCoupon.getCouponId())
                .couponCode(rateDiscountCoupon.getCouponCode())
                .couponValid(rateDiscountCoupon.isCouponValid())
                .couponValidDays(rateDiscountCoupon.getCouponValidDays())
                .createdDate(rateDiscountCoupon.getCreatedDate())
                .discountRate(rateDiscountCoupon.getDiscountRate())
                .build();
    }
}