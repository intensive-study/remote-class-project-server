package org.server.remoteclass.service.rateDiscount;

import org.server.remoteclass.dto.rateDiscount.RequestRateDiscountDto;
import org.server.remoteclass.dto.rateDiscount.ResponseRateDiscountDto;

import java.util.List;

public interface RateDiscountService {
    //정액할인 쿠폰 생성
    void createRateDiscountCoupon(RequestRateDiscountDto requestRateDiscountDto);
    List<ResponseRateDiscountDto> getAllRateDiscountCoupons();
    ResponseRateDiscountDto getRateDiscountCoupon(Long couponId);
}
