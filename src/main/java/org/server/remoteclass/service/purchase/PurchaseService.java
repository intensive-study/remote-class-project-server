package org.server.remoteclass.service.purchase;

import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.dto.purchase.PurchaseDto;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.exception.IdNotExistException;

public interface PurchaseService {

    PurchaseDto createPurchase(RequestPurchaseDto requestPurchaseDto) throws IdNotExistException;

}
