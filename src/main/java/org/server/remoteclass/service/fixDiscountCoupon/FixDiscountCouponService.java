package org.server.remoteclass.service.fixDiscountCoupon;

import org.server.remoteclass.dto.fixDiscountCoupon.RequestFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.ResponseFixDiscountCouponDto;

import java.util.List;

public interface FixDiscountCouponService {
    //정률할인 쿠폰 생성
    void createFixDiscountCoupon(RequestFixDiscountCouponDto requestFixDiscountCouponDto);
    List<ResponseFixDiscountCouponDto> getAllFixDiscountCoupons();
    ResponseFixDiscountCouponDto getFixDiscountCoupon(Long couponId);
}
