package org.server.remoteclass.service.coupon;

import org.server.remoteclass.dto.coupon.*;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface CouponService {

    //일단 다 관리자 권한이라 가정하겠습니다.
    ResponseCouponDto getCouponByCouponId(Long couponId);
    //쿠폰 생성
    ResponseCouponDto createCoupon(RequestCouponDto requestCouponDto);
    //쿠폰 비활성화
    void deactivateCoupon(Long couponId) throws IdNotExistException;
    //모든 쿠폰 조회
    List<ResponseCouponDto> getAllCoupons();
    //쿠폰 코드로 쿠폰 조회
    ResponseCouponDto getCouponByCouponCode(String couponCode);
    //쿠폰 삭제
    void deleteCoupon(Long couponId) throws IdNotExistException;
}
