package org.server.remoteclass.service;

import org.server.remoteclass.dto.CouponDto;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface CouponService {

    //쿠폰 상세보기(관리자/사용자 역할을 나눠야 할 것 같습니다.)
    CouponDto getCouponByCouponId(Long couponId);
    //쿠폰 생성
    CouponDto createCoupon(CouponDto couponDto);
    //쿠폰 비활성화
    CouponDto deactivateCoupon(Long couponId) throws IdNotExistException;
    //모든 쿠폰 조회
    List<CouponDto> getAllCoupons();
    //쿠폰 코드로 쿠폰 조회
    CouponDto getCouponByCouponCode(String couponCode);
}
