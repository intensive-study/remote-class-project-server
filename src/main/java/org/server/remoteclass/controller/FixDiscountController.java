package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.fixDiscount.RequestFixDiscountDto;
import org.server.remoteclass.dto.fixDiscount.ResponseFixDiscountDto;
import org.server.remoteclass.service.fixDiscount.FixDiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fix-discount")
public class FixDiscountController {

    private final FixDiscountService fixDiscountService;

    public FixDiscountController(FixDiscountService fixDiscountService){
        this.fixDiscountService = fixDiscountService;
    }

    // 정액 할인 쿠폰 생성(관리자 권한)
    @ApiOperation(value = "정액 할인 쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping
    public ResponseEntity createFixDiscountCoupon(@RequestBody @Valid RequestFixDiscountDto fixDiscountDto){
        fixDiscountService.createFixDiscountCoupon(fixDiscountDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 정액 할인 쿠폰 전체 조회
    @ApiOperation(value = "정액 할인 쿠폰 전체 조회")
    @GetMapping
    public ResponseEntity<List<ResponseFixDiscountDto>> getAllFixDiscountCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(fixDiscountService.getAllFixDiscountCoupons());
    }

    // 정액 할인 쿠폰 개별 조회
    @ApiOperation(value = "정액 할인 쿠폰을 쿠폰 번호로 조회")
    @GetMapping("/{couponId}")
    public ResponseEntity<ResponseFixDiscountDto> getFixDiscountCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.CREATED).body(fixDiscountService.getFixDiscountCoupon(couponId));
    }
}
