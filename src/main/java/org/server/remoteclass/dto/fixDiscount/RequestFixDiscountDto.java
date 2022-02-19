package org.server.remoteclass.dto.fixDiscount;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFixDiscountDto {
    private String title;
    private int couponValidDays;
    private int discountPrice;
}
