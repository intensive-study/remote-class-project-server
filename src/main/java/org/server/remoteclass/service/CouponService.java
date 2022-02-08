package org.server.remoteclass.service;

import org.server.remoteclass.dto.CouponDto;

import java.util.List;

public interface CouponService {

    //쿠폰 상세보기
    CouponDto getCouponByCouponId(Long couponId);
    //쿠폰 생성
    CouponDto createCoupon(CouponDto couponDto);
    //모든 쿠폰 조회
    List<CouponDto> getAllCoupons();
    //쿠폰 코드로 쿠폰 조회
    CouponDto getCouponByCouponCode(String couponCode);
}
