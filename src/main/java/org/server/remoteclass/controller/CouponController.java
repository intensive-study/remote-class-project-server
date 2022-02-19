package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.service.coupon.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService){
        this.couponService = couponService;
    }

    //관리자 권한이므로 CouponDto로 모든 정보를 보여주게끔 한다.
    @ApiOperation(value = "전체 쿠폰 조회", notes = "현재까지 생성된 모든 쿠폰을 조회할 수 있다.")
    @GetMapping("/coupons")
    public ResponseEntity<List<ResponseCouponDto>> getAllCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getAllCoupons());
    }

    //쿠폰 번호로 쿠폰 검색(관리자 권한)
    @ApiOperation(value = "쿠폰 번호로 쿠폰 조회", notes = "쿠폰 번호에 해당하는 쿠폰을 조회한다.")
    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<ResponseCouponDto> getCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getCouponByCouponId(couponId));
    }

}
