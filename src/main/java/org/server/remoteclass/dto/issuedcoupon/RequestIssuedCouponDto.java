package org.server.remoteclass.dto.issuedcoupon;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestIssuedCouponDto {
    @NotEmpty
    private String couponCode;
}
