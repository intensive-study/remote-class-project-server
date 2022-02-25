package org.server.remoteclass.service.purchase;

import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.dto.purchase.ResponsePurchaseDto;

import java.util.List;

public interface PurchaseService {

    void createPurchase(RequestPurchaseDto requestPurchaseDto);
    void cancel(Long lectureId);
    void cancelAll(Long purchaseId);
    List<ResponsePurchaseDto> getAllPurchaseByUserId();
    ResponsePurchaseDto getPurchaseByUserIdAndPurchaseId(Long purchaseId);

    //관리자
    List<ResponsePurchaseDto> getPurchaseByUserIdByAdmin(Long userId);

}
