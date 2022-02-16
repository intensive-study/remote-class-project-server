package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.fixDiscount.RequestFixDiscountDto;
import org.server.remoteclass.service.fixDiscount.FixDiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/fix-discount")
public class FixDiscountController {

    private final FixDiscountService fixDiscountService;

    public FixDiscountController(FixDiscountService fixDiscountService){
        this.fixDiscountService = fixDiscountService;
    }

    //쿠폰 생성(관리자 권한)
    @ApiOperation(value = "정액 할인 쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping
    public ResponseEntity createFixDiscountCoupon(@RequestBody @Valid RequestFixDiscountDto fixDiscountDto){
        fixDiscountService.createFixDiscountCoupon(fixDiscountDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
