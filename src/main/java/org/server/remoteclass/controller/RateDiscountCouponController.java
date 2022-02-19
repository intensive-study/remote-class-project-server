package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.rateDiscountCoupon.RequestRateDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.ResponseRateDiscountCouponDto;
import org.server.remoteclass.service.rateDiscountCoupon.RateDiscountCouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rate-discount")
public class RateDiscountCouponController {

    private final RateDiscountCouponService rateDiscountCouponService;

    public RateDiscountCouponController(RateDiscountCouponService rateDiscountCouponService){
        this.rateDiscountCouponService = rateDiscountCouponService;
    }

    // 정률 할인 쿠폰 생성(관리자 권한)
    @ApiOperation(value = "정률 할인 쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping
    public ResponseEntity createRateDiscountCoupon(@RequestBody @Valid RequestRateDiscountCouponDto requestRateDiscountCouponDto){
        rateDiscountCouponService.createRateDiscountCoupon(requestRateDiscountCouponDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 정률 할인 쿠폰 전체 조회
    @ApiOperation(value = "정률 할인 쿠폰 전체 조회")
    @GetMapping
    public ResponseEntity<List<ResponseRateDiscountCouponDto>> getAllRateDiscountCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(rateDiscountCouponService.getAllRateDiscountCoupons());
    }

    // 정률 할인 쿠폰 개별 조회
    @ApiOperation(value = "정률 할인 쿠폰을 쿠폰 번호로 조회")
    @GetMapping("/{couponId}")
    public ResponseEntity<ResponseRateDiscountCouponDto> getRateDiscountCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateDiscountCouponService.getRateDiscountCoupon(couponId));
    }
}
