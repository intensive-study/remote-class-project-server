package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.ResponseFixDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.ResponseRateDiscountCouponDto;
import org.server.remoteclass.service.coupon.CouponService;
import org.server.remoteclass.service.fixDiscountCoupon.FixDiscountCouponService;
import org.server.remoteclass.service.rateDiscountCoupon.RateDiscountCouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final FixDiscountCouponService fixDiscountCouponService;
    private final RateDiscountCouponService rateDiscountCouponService;

    public CouponController(CouponService couponService,
                            FixDiscountCouponService fixDiscountCouponService,
                            RateDiscountCouponService rateDiscountCouponService){
        this.couponService = couponService;
        this.fixDiscountCouponService = fixDiscountCouponService;
        this.rateDiscountCouponService = rateDiscountCouponService;
    }

    //CouponDto로 모든 정보를 보여주게끔 한다.
    @ApiOperation(value = "전체 쿠폰 조회", notes = "현재까지 생성된 모든 쿠폰을 조회할 수 있다.")
    @GetMapping
    public ResponseEntity<List<ResponseCouponDto>> getAllCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getAllCoupons());
    }

    //쿠폰 번호로 쿠폰 검색
    @ApiOperation(value = "쿠폰 번호로 쿠폰 조회", notes = "쿠폰 번호에 해당하는 쿠폰을 조회한다.")
    @GetMapping("/{couponId}")
    public ResponseEntity<ResponseCouponDto> getCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getCouponByCouponId(couponId));
    }

    // 정액 할인 쿠폰 전체 조회
    @ApiOperation(value = "정액 할인 쿠폰 전체 조회")
    @GetMapping("/fix-discount")
    public ResponseEntity<List<ResponseFixDiscountCouponDto>> getAllFixDiscountCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(fixDiscountCouponService.getAllFixDiscountCoupons());
    }

    // 정액 할인 쿠폰 개별 조회
    @ApiOperation(value = "정액 할인 쿠폰을 쿠폰 번호로 조회")
    @GetMapping("/fix-discount/{couponId}")
    public ResponseEntity<ResponseFixDiscountCouponDto> getFixDiscountCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.OK).body(fixDiscountCouponService.getFixDiscountCoupon(couponId));
    }

    // 정률 할인 쿠폰 전체 조회
    @ApiOperation(value = "정률 할인 쿠폰 전체 조회")
    @GetMapping("/rate-discount")
    public ResponseEntity<List<ResponseRateDiscountCouponDto>> getAllRateDiscountCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(rateDiscountCouponService.getAllRateDiscountCoupons());
    }

    // 정률 할인 쿠폰 개별 조회
    @ApiOperation(value = "정률 할인 쿠폰을 쿠폰 번호로 조회")
    @GetMapping("/rate-discount/{couponId}")
    public ResponseEntity<ResponseRateDiscountCouponDto> getRateDiscountCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateDiscountCouponService.getRateDiscountCoupon(couponId));
    }

}
