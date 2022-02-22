package org.server.remoteclass.dto.coupon;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCouponDto {
    @NotNull
    private int couponValidDays;
}
