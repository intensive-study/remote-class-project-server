package org.server.remoteclass.service.purchase;

import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.dto.purchase.ResponsePurchaseDto;

import java.util.List;

public interface PurchaseService {

    void createPurchase(RequestPurchaseDto requestPurchaseDto);
    void cancelPurchase(Long lectureId);
    void cancelAllPurchases(Long purchaseId);
    List<ResponsePurchaseDto> getAllPurchasesByUserId();
    ResponsePurchaseDto getPurchaseByUserIdAndPurchaseId(Long purchaseId);

    //관리자
    List<ResponsePurchaseDto> getAllPurchasesByUserIdByAdmin();
    List<ResponsePurchaseDto> getPurchaseByUserIdByAdmin(Long userId);

}
