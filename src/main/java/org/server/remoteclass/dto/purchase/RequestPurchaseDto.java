package org.server.remoteclass.dto.purchase;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class RequestPurchaseDto {
    @NotEmpty
    private Long orderId;
}
