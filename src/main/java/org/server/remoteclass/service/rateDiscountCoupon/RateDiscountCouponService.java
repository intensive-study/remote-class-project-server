package org.server.remoteclass.service.rateDiscountCoupon;

import org.server.remoteclass.dto.rateDiscountCoupon.RequestRateDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.RequestUpdateRateDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.ResponseRateDiscountCouponDto;

import java.util.List;

public interface RateDiscountCouponService {
    //정액할인 쿠폰 생성
    ResponseRateDiscountCouponDto createRateDiscountCoupon(RequestRateDiscountCouponDto requestRateDiscountCouponDto);
    void updateRateDiscountCoupon(RequestUpdateRateDiscountCouponDto requestUpdateRateDiscountCouponDto);
    List<ResponseRateDiscountCouponDto> getAllRateDiscountCoupons();
    ResponseRateDiscountCouponDto getRateDiscountCoupon(Long couponId);
}
