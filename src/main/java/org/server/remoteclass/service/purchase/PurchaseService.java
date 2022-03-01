package org.server.remoteclass.service.purchase;

import org.server.remoteclass.dto.order.RequestCancelPartialPurchaseDto;
import org.server.remoteclass.dto.purchase.ResponsePurchaseByAdminDto;
import org.server.remoteclass.dto.purchase.ResponsePurchaseDto;

import java.util.List;

public interface PurchaseService {

    void createPurchase(Long orderId);
    void cancelPartialPurchases(Long purchaseId, RequestCancelPartialPurchaseDto requestCancelPartialPurchaseDto);
    void cancelAllPurchases(Long purchaseId);
    List<ResponsePurchaseDto> getAllPurchasesByUserId();
    ResponsePurchaseDto getPurchaseByUserIdAndPurchaseId(Long purchaseId);

    //관리자
    List<ResponsePurchaseByAdminDto> getAllPurchasesByUserIdByAdmin();
    List<ResponsePurchaseByAdminDto> getPurchaseByUserIdByAdmin(Long userId);

}
