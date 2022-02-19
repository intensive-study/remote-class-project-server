package org.server.remoteclass.dto.rateDiscount;

import lombok.*;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.RateDiscount;

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

    public static ResponseRateDiscountDto from(RateDiscount rateDiscount){
        if(rateDiscount == null) return null;
        return ResponseRateDiscountDto.builder()
                .couponId(rateDiscount.getCouponId())
                .couponCode(rateDiscount.getCouponCode())
                .couponValid(rateDiscount.isCouponValid())
                .couponValidDays(rateDiscount.getCouponValidDays())
                .createdDate(rateDiscount.getCreatedDate())
                .discountRate(rateDiscount.getDiscountRate())
                .build();
    }
}
