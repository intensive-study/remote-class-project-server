package org.server.remoteclass.dto.rateDiscountCoupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRateDiscountCouponDto {
    @NotEmpty
    private String title;
    @NotNull
    private int couponValidDays;
    @NotNull
    private int discountRate;
}
