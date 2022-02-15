package org.server.remoteclass.dto.coupon;

import lombok.*;
import org.server.remoteclass.entity.Coupon;

import java.time.LocalDateTime;

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

    public static ResponseCouponDto from(Coupon coupon){
        if(coupon == null) return null;
        return ResponseCouponDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .couponValid(coupon.isCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCratedDate())
                .build();
    }
}
