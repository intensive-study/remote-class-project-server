package org.server.remoteclass.dto.purchase;

import lombok.*;
import org.server.remoteclass.entity.Purchase;

import java.time.LocalDateTime;


@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ResponsePurchaseDto {

    private Long purchaseId;
    private Long orderId;
    private Integer purchasePrice;
    private LocalDateTime purchaseDate;

    public ResponsePurchaseDto(Purchase purchase) {
        this.purchaseId = purchase.getPurchaseId();
        this.orderId = purchase.getOrder().getOrderId();
        this.purchasePrice = purchase.getPurchasePrice();
        this.purchaseDate = purchase.getPurchaseDate();
    }

    public static ResponsePurchaseDto from(Purchase purchase){
        if(purchase == null) return null;
        return ResponsePurchaseDto.builder()
                .purchaseId(purchase.getPurchaseId())
                .orderId(purchase.getOrder().getOrderId())
                .purchasePrice(purchase.getPurchasePrice())
                .purchaseDate(purchase.getPurchaseDate())
                .build();
    }

}
