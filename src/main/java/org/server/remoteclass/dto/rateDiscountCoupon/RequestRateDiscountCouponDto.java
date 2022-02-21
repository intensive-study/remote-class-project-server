package org.server.remoteclass.dto.rateDiscountCoupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRateDiscountCouponDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private int couponValidDays;
    @NotEmpty
    private int discountRate;
}
