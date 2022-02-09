package org.server.remoteclass.controller;

import org.server.remoteclass.dto.CouponDto;
import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.LectureFormDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.CouponService;
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
    @GetMapping
    public ResponseEntity<List<CouponDto>> getAllCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getAllCoupons());
    }

    //쿠폰 번호로 쿠폰 검색(관리자 권한)
    @GetMapping("/{couponId}")
    public ResponseEntity<CouponDto> getCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getCouponByCouponId(couponId));
    }

    //쿠폰 생성(관리자 권한)
    @PostMapping
    public ResponseEntity<CouponDto> createCoupon(@RequestBody @Valid CouponDto couponDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(couponService.createCoupon(couponDto));
    }

    //쿠폰 비활성화(관리자 권한)
    @PutMapping("/deactivate/{couponId}")
    public ResponseEntity<CouponDto> createCoupon(@PathVariable("couponId") Long couponId) throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.OK).body(couponService.deactivateCoupon(couponId));
    }
}
