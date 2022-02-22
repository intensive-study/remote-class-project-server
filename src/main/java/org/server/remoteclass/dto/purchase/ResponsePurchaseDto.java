package org.server.remoteclass.dto.purchase;

import lombok.*;
import org.server.remoteclass.entity.Purchase;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ResponsePurchaseDto {

    private Long purchaseId;
    @NotNull // Response에도 NotNull이 있어도 될까요..? 크게 상관은 없겠지만 추후에 이슈 발생 시에 이 dto에만 조건이 있어서 찾기 어려울 수도 있을 것 같아요
    private Long orderId;
    @NotNull
    private Integer purchasePrice;
    @NotNull
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
