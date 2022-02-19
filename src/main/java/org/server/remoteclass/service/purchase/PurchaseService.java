package org.server.remoteclass.service.purchase;

import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.dto.purchase.PurchaseDto;
import org.server.remoteclass.dto.purchase.ResponsePurchaseDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface PurchaseService {

    PurchaseDto createPurchase(RequestPurchaseDto requestPurchaseDto);
    List<ResponsePurchaseDto> getAllPurchaseByUserId();
    ResponsePurchaseDto getPurchaseByUserIdAndPurchaseId(Long purchaseId);

}
