package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.fixDiscountCoupon.RequestFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.ResponseFixDiscountCouponDto;
import org.server.remoteclass.service.fixDiscountCoupon.FixDiscountCouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fix-discount")
public class FixDiscountCouponController {

    private final FixDiscountCouponService fixDiscountCouponService;

    public FixDiscountCouponController(FixDiscountCouponService fixDiscountCouponService){
        this.fixDiscountCouponService = fixDiscountCouponService;
    }

    // 정액 할인 쿠폰 생성(관리자 권한)
    @ApiOperation(value = "정액 할인 쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping
    public ResponseEntity createFixDiscountCoupon(@RequestBody @Valid RequestFixDiscountCouponDto fixDiscountDto){
        fixDiscountCouponService.createFixDiscountCoupon(fixDiscountDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 정액 할인 쿠폰 전체 조회
    @ApiOperation(value = "정액 할인 쿠폰 전체 조회")
    @GetMapping
    public ResponseEntity<List<ResponseFixDiscountCouponDto>> getAllFixDiscountCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(fixDiscountCouponService.getAllFixDiscountCoupons());
    }

    // 정액 할인 쿠폰 개별 조회
    @ApiOperation(value = "정액 할인 쿠폰을 쿠폰 번호로 조회")
    @GetMapping("/{couponId}")
    public ResponseEntity<ResponseFixDiscountCouponDto> getFixDiscountCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.OK).body(fixDiscountCouponService.getFixDiscountCoupon(couponId));
    }
}
