package org.server.remoteclass.dto.fixDiscountCoupon;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFixDiscountCouponDto {

    @NotEmpty
    private String title;
    @NotNull
    @Min(0)
    private Integer couponValidDays;
    @NotNull
    @Min(0)
    private Integer discountPrice;
}
