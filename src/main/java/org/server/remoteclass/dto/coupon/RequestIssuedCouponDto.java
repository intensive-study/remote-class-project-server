package org.server.remoteclass.dto.coupon;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestIssuedCouponDto {
    private String couponCode;
}
