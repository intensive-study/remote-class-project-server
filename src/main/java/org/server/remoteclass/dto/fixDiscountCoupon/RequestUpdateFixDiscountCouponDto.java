package org.server.remoteclass.dto.fixDiscountCoupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateFixDiscountCouponDto {

    @NotNull
    @Min(1)
    private Long couponId;
    @NotEmpty
    private String title;
    @NotNull
    @Min(0)
    private Integer couponValidDays;
    @NotNull
    @Min(0)
    private Integer discountPrice;
}
