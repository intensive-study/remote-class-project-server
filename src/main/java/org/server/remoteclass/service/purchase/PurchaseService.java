package org.server.remoteclass.service.purchase;

import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.dto.purchase.ResponsePurchaseDto;

import java.util.List;

public interface PurchaseService {

    void createPurchase(RequestPurchaseDto requestPurchaseDto);
    List<ResponsePurchaseDto> getAllPurchaseByUserId();
    ResponsePurchaseDto getPurchaseByUserIdAndPurchaseId(Long purchaseId);

}
