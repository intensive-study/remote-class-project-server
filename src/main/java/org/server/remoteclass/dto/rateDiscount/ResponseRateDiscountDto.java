package org.server.remoteclass.dto.rateDiscount;

import lombok.*;
import org.server.remoteclass.entity.Coupon;

import java.time.LocalDateTime;

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
    private int discountRate;

    public static ResponseRateDiscountDto from(Coupon coupon){
        if(coupon == null) return null;
        return ResponseRateDiscountDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .couponValid(coupon.isCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCreatedDate())
                .discountRate(coupon.getDiscountRate())
                .build();
    }
}
