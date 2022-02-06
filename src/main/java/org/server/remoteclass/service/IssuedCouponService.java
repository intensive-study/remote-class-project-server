package org.server.remoteclass.service;

import org.server.remoteclass.dto.CouponDto;
import org.server.remoteclass.dto.IssuedCouponDto;

import java.util.List;

public interface IssuedCouponService {

    //발급받은 쿠폰 상세보기
    IssuedCouponDto getIssuedCouponByIssuedCouponId(Long IssuedCouponId);

    CouponDto getCouponByCouponId(Long couponId);
    //쿠폰 생성
    CouponDto createCoupon(CouponDto couponDto);
    //모든 쿠폰 조회 (관리자 기능)
    List<CouponDto> getAllCoupons();
}
