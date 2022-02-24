package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.purchase.*;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.purchase.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService){
        this.purchaseService = purchaseService;
    }

    @ApiOperation(value = "구매 생성", notes = "구매 생성으로 구매 완료처리함.")
    @PostMapping
    public ResponseEntity createPurchase(@RequestBody @Valid RequestPurchaseDto requestPurchaseDto) {
        purchaseService.createPurchase(requestPurchaseDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "구매 내역 전체 조회", notes = "생성된 전체 구매 내열 조회함.")
    @GetMapping
    public ResponseEntity<List<ResponsePurchaseDto>> getAllPurchase() throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.getAllPurchaseByUserId());
    }

    @ApiOperation(value = "특정 구매 내역 조회", notes = "생성된 특정 구매 내열 조회함.")
    @GetMapping("/{purchaseId}")
    public ResponseEntity<ResponsePurchaseDto> getAllPurchase(@PathVariable("purchaseId") @Valid Long purchaseId) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.getPurchaseByUserIdAndPurchaseId(purchaseId));
    }
}