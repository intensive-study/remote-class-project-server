package org.server.remoteclass.dto.purchase;

import lombok.*;
import org.server.remoteclass.entity.Purchase;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class PurchaseDto {

    private Long purchaseId;
    @NotNull
    private Long orderId;
    @NotNull
    private Integer purchasePrice;
    @NotNull
    private LocalDateTime purchaseDate;

    public static PurchaseDto from(Purchase purchase){
        if(purchase == null) return null;
        return PurchaseDto.builder()
                .purchaseId(purchase.getPurchaseId())
                .orderId(purchase.getOrder().getOrderId())
                .purchasePrice(purchase.getPurchasePrice())
                .purchaseDate(purchase.getPurchaseDate())
                .build();
    }

}
