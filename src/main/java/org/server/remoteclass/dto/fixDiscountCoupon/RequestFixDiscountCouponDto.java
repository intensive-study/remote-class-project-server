package org.server.remoteclass.dto.fixDiscountCoupon;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFixDiscountCouponDto {
    private String title;
    private int couponValidDays;
    private int discountPrice;
}
