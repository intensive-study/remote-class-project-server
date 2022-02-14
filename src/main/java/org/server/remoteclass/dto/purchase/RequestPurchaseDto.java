package org.server.remoteclass.dto.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPurchaseDto {
    private Long orderId;
}
