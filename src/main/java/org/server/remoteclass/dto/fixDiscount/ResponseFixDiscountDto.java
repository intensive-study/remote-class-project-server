package org.server.remoteclass.dto.fixDiscount;

import lombok.*;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.FixDiscount;

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

    public static ResponseFixDiscountDto from(FixDiscount fixDiscount){
        if(fixDiscount == null) return null;
        return ResponseFixDiscountDto.builder()
                .couponId(fixDiscount.getCouponId())
                .couponCode(fixDiscount.getCouponCode())
                .couponValid(fixDiscount.isCouponValid())
                .couponValidDays(fixDiscount.getCouponValidDays())
                .createdDate(fixDiscount.getCreatedDate())
                .title(fixDiscount.getTitle())
                .discountPrice(fixDiscount.getDiscountPrice())
                .build();
    }
}
