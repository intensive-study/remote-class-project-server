package org.server.remoteclass.dto.coupon;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCouponDto {
    private int couponValidDays;
}
