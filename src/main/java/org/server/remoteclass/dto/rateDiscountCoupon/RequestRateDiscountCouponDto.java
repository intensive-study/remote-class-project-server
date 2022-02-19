package org.server.remoteclass.dto.rateDiscountCoupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRateDiscountCouponDto {
    private String title;
    private int couponValidDays;
    private int discountRate;
}
