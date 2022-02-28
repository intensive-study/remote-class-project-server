package org.server.remoteclass.service.fixDiscountCoupon;

import org.server.remoteclass.dto.fixDiscountCoupon.RequestFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.RequestUpdateFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.ResponseFixDiscountCouponDto;

import java.util.List;

public interface FixDiscountCouponService {
    //정률할인 쿠폰 생성
    ResponseFixDiscountCouponDto createFixDiscountCoupon(RequestFixDiscountCouponDto requestFixDiscountCouponDto);
    void updateFixDiscountCoupon(RequestUpdateFixDiscountCouponDto requestUpdateFixDiscountCouponDto);
    List<ResponseFixDiscountCouponDto> getAllFixDiscountCoupons();
    ResponseFixDiscountCouponDto getFixDiscountCoupon(Long couponId);
}
