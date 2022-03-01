package org.server.remoteclass.dto.rateDiscountCoupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
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
    @Min(0)
    private Integer couponValidDays;
    @NotNull
    @Range(min = 0, max = 100)
    private Integer discountRate;
}
