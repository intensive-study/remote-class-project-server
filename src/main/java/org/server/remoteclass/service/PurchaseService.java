package org.server.remoteclass.service;

import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.dto.purchase.PurchaseDto;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.exception.IdNotExistException;

public interface PurchaseService {

    PurchaseDto createPurchase(RequestPurchaseDto requestPurchaseDto) throws IdNotExistException;
    Integer calculateOrder(Order order);
    Integer discountOrder(Order order);
}
