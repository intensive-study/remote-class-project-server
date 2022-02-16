package org.server.remoteclass.dto.fixDiscount;

import lombok.*;
import org.server.remoteclass.entity.Coupon;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFixDiscountDto {

    private Long couponId;
    private String couponCode;
    private boolean couponValid;

    private String title;
    private int couponValidDays;
    private int discountPrice;

    private LocalDateTime createdDate;

    public static ResponseFixDiscountDto from(Coupon coupon){
        if(coupon == null) return null;
        return ResponseFixDiscountDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .couponValid(coupon.isCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCreatedDate())
                .title(coupon.getTitle())
                .discountPrice(coupon.getDiscountPrice())
                .build();
    }
}
