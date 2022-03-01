package org.server.remoteclass.dto.purchase;

import lombok.*;
import org.server.remoteclass.entity.Purchase;

import java.time.LocalDateTime;


@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ResponsePurchaseByAdminDto {

    private Long purchaseId;
    private Long orderId;
    private Integer purchasePrice;
    private LocalDateTime purchaseDate;
    private Long userId;

    public ResponsePurchaseByAdminDto(Purchase purchase) {
        this.purchaseId = purchase.getPurchaseId();
        this.orderId = purchase.getOrder().getOrderId();
        this.purchasePrice = purchase.getPurchasePrice();
        this.purchaseDate = purchase.getPurchaseDate();
        this.userId = purchase.getOrder().getUser().getUserId();
    }

    public static ResponsePurchaseByAdminDto from(Purchase purchase){
        if(purchase == null) return null;
        return ResponsePurchaseByAdminDto.builder()
                .purchaseId(purchase.getPurchaseId())
                .orderId(purchase.getOrder().getOrderId())
                .purchasePrice(purchase.getPurchasePrice())
                .purchaseDate(purchase.getPurchaseDate())
                .userId(purchase.getOrder().getUser().getUserId())
                .build();
    }

}
