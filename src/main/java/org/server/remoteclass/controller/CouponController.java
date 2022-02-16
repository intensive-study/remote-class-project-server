package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.coupon.RequestCouponDto;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.dto.fixDiscount.RequestFixDiscountDto;
import org.server.remoteclass.dto.rateDiscount.RequestRateDiscountDto;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.coupon.CouponService;
import org.server.remoteclass.service.fixDiscount.FixDiscountService;
import org.server.remoteclass.service.rateDiscount.RateDiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @GetMapping
    public ResponseEntity<List<ResponseCouponDto>> getAllCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getAllCoupons());
    }

    //쿠폰 번호로 쿠폰 검색(관리자 권한)
    @ApiOperation(value = "쿠폰 번호로 쿠폰 조회", notes = "쿠폰 번호에 해당하는 쿠폰을 조회한다.")
    @GetMapping("/{couponId}")
    public ResponseEntity<ResponseCouponDto> getCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getCouponByCouponId(couponId));
    }

    //쿠폰 생성(관리자 권한)
    @ApiOperation(value = "쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping
    public ResponseEntity createCoupon(@RequestBody @Valid RequestCouponDto requestCouponDto){
        couponService.createCoupon(requestCouponDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //쿠폰 비활성화(관리자 권한)
    @ApiOperation(value = "쿠폰 비활성화", notes = "더 이상 쿠폰을 발급받을 수 없게 쿠폰을 비활성화 한다.")
    @PutMapping("/deactivate/{couponId}")
    public ResponseEntity createCoupon(@PathVariable("couponId") Long couponId) throws IdNotExistException {
        couponService.deactivateCoupon(couponId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "쿠폰 삭제", notes = "쿠폰 목록에서 쿠폰을 삭제한다.")
    @DeleteMapping("/{couponId}")
    public ResponseEntity deleteCoupon(@PathVariable("couponId") Long couponId) throws IdNotExistException {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
