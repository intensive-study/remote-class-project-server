package org.server.remoteclass.dto.purchase;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class RequestPurchaseDto {
    @NotNull
    @Min(0)
    private Long orderId;
}
