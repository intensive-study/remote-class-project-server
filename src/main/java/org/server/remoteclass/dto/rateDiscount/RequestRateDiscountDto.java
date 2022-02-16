package org.server.remoteclass.dto.rateDiscount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestRateDiscountDto {
    private String title;
    private int couponValidDays;
    private int rateDiscount;
}
