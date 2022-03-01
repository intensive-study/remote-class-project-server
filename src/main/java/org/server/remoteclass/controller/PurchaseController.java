package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.order.RequestCancelPartialPurchaseDto;
import org.server.remoteclass.dto.purchase.*;
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

//    @ApiOperation(value = "구매 생성", notes = "구매 생성으로 구매 완료처리함.")
//    @PostMapping
//    public ResponseEntity createPurchase(@RequestBody @Valid RequestPurchaseDto requestPurchaseDto) {
//        purchaseService.createPurchase(requestPurchaseDto);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @ApiOperation(value = "주문 부분 취소", notes = "학생이 신청했던 강의 일부를 리스트로 입력받아 취소할 수 있다.")
    @DeleteMapping("/partial/{purchaseId}")
    public ResponseEntity cancelPartialPurchases(@PathVariable("purchaseId") Long purchaseId,
                                         @RequestBody RequestCancelPartialPurchaseDto requestCancelPartialPurchaseDto) {
        purchaseService.cancelPartialPurchases(purchaseId, requestCancelPartialPurchaseDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "주문 전체 취소", notes = "학생이 신청했던 강의 전체를 취소할 수 있다.")
    @DeleteMapping("/{purchaseId}")
    public ResponseEntity cancelAllPurchase(@PathVariable("purchaseId") Long purchaseId) {
        purchaseService.cancelAllPurchases(purchaseId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "구매 내역 전체 조회", notes = "사용자의 생성된 전체 구매 내역 조회함.")
    @GetMapping
    public ResponseEntity<List<ResponsePurchaseDto>> getMyAllPurchases() {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.getAllPurchasesByUserId());
    }

    @ApiOperation(value = "특정 구매 내역 조회", notes = "생성된 특정 구매 내역 조회함.")
    @GetMapping("/{purchaseId}")
    public ResponseEntity<ResponsePurchaseDto> getMyOnePurchase(@PathVariable("purchaseId") @Valid Long purchaseId) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.getPurchaseByUserIdAndPurchaseId(purchaseId));
    }
}