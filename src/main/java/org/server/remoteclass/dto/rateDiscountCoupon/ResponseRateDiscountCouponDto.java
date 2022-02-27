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
    private String title;
    private Boolean couponValid;
    private Integer couponValidDays;
    private LocalDateTime createdDate;
    private Integer discountRate;

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
