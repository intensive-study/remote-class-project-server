package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.rateDiscount.RequestRateDiscountDto;
import org.server.remoteclass.dto.rateDiscount.ResponseRateDiscountDto;
import org.server.remoteclass.service.rateDiscount.RateDiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rate-discount")
public class RateDiscountController {

    private final RateDiscountService rateDiscountService;

    public RateDiscountController(RateDiscountService rateDiscountService){
        this.rateDiscountService = rateDiscountService;
    }

    // 정률 할인 쿠폰 생성(관리자 권한)
    @ApiOperation(value = "정률 할인 쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping
    public ResponseEntity createRateDiscountCoupon(@RequestBody @Valid RequestRateDiscountDto requestRateDiscountDto){
        rateDiscountService.createRateDiscountCoupon(requestRateDiscountDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 정률 할인 쿠폰 전체 조회
    @ApiOperation(value = "정률 할인 쿠폰 전체 조회")
    @GetMapping
    public ResponseEntity<List<ResponseRateDiscountDto>> getAllRateDiscountCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(rateDiscountService.getAllRateDiscountCoupons());
    }

    // 정률 할인 쿠폰 개별 조회
    @ApiOperation(value = "정률 할인 쿠폰을 쿠폰 번호로 조회")
    @GetMapping("/{couponId}")
    public ResponseEntity<ResponseRateDiscountDto> getRateDiscountCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateDiscountService.getRateDiscountCoupon(couponId));
    }
}
