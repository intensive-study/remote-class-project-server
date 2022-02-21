package org.server.remoteclass.dto.fixDiscountCoupon;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFixDiscountCouponDto {

    @NotEmpty
    private String title;
    @NotEmpty
    private int couponValidDays;
    @NotEmpty
    private int discountPrice;
}
