package org.server.remoteclass.dto.coupon;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCouponDto {
    private int couponValidDays;
}
