package org.server.remoteclass.service;

import org.server.remoteclass.dto.coupon.CouponDto;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface CouponService {

    //일단 다 관리자 권한이라 가정하겠습니다.
    CouponDto getCouponByCouponId(Long couponId);
    //쿠폰 생성
    CouponDto createCoupon(CouponDto couponDto);
    //쿠폰 비활성화
    CouponDto deactivateCoupon(Long couponId) throws IdNotExistException;
    //모든 쿠폰 조회
    List<CouponDto> getAllCoupons();
    //쿠폰 코드로 쿠폰 조회
    CouponDto getCouponByCouponCode(String couponCode);
    //쿠폰 삭제
    CouponDto deleteCoupon(Long couponId) throws IdNotExistException;
}
