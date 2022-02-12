package org.server.remoteclass.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RequestIssuedCouponDto {
    private String couponCode;
}
