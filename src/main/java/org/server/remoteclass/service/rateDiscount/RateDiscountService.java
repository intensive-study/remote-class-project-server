package org.server.remoteclass.service.rateDiscount;

import org.server.remoteclass.dto.rateDiscount.RequestRateDiscountDto;

public interface RateDiscountService {
    //정액할인 쿠폰 생성
    void createRateDiscountCoupon(RequestRateDiscountDto requestRateDiscountDto);
}
