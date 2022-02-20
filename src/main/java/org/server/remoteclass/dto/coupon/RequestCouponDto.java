package org.server.remoteclass.dto.coupon;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCouponDto {
    @NotEmpty
    private int couponValidDays;
}
