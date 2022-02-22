package org.server.remoteclass.dto.fixDiscountCoupon;

import lombok.*;

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
    private int couponValidDays;
    @NotNull
    private int discountPrice;
}
