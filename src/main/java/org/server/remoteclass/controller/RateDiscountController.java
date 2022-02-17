package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.rateDiscount.RequestRateDiscountDto;
import org.server.remoteclass.service.rateDiscount.RateDiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/rate-discount")
public class RateDiscountController {

    private final RateDiscountService rateDiscountService;

    public RateDiscountController(RateDiscountService rateDiscountService){
        this.rateDiscountService = rateDiscountService;
    }

    //쿠폰 생성(관리자 권한)
    @ApiOperation(value = "정률 할인 쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping
    public ResponseEntity createRateDiscountCoupon(@RequestBody @Valid RequestRateDiscountDto requestRateDiscountDto){
        rateDiscountService.createRateDiscountCoupon(requestRateDiscountDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
