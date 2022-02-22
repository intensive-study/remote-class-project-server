package org.server.remoteclass.dto.fixDiscountCoupon;

import lombok.*;
import org.server.remoteclass.entity.FixDiscountCoupon;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFixDiscountCouponDto {

    private Long couponId;
    private String couponCode;
    private Boolean couponValid;

    private String title;
    private Integer couponValidDays;
    private Integer discountPrice;

    private LocalDateTime createdDate;

    public static ResponseFixDiscountCouponDto from(FixDiscountCoupon fixDiscountCoupon){
        if(fixDiscountCoupon == null) return null;
        return ResponseFixDiscountCouponDto.builder()
                .couponId(fixDiscountCoupon.getCouponId())
                .couponCode(fixDiscountCoupon.getCouponCode())
                .couponValid(fixDiscountCoupon.isCouponValid())
                .couponValidDays(fixDiscountCoupon.getCouponValidDays())
                .createdDate(fixDiscountCoupon.getCreatedDate())
                .title(fixDiscountCoupon.getTitle())
                .discountPrice(fixDiscountCoupon.getDiscountPrice())
                .build();
    }
}
