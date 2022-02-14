package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.purchase.PurchaseDto;
import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService){
        this.purchaseService = purchaseService;
    }

    @ApiOperation(value = "구매 생성", notes = "구매 생성으로 구매 완료처리함.")
    @PostMapping
    public ResponseEntity<PurchaseDto> createPurchase(@RequestBody @Valid RequestPurchaseDto requestPurchaseDto) throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.createPurchase(requestPurchaseDto));
    }
}
