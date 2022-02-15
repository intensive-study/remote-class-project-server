package org.server.remoteclass.dto.issuedcoupon;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestIssuedCouponDto {
    private String couponCode;
}
