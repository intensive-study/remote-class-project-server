package org.server.remoteclass.service.fixDiscount;

import org.server.remoteclass.dto.fixDiscount.RequestFixDiscountDto;

public interface FixDiscountService {
    //정률할인 쿠폰 생성
    void createFixDiscountCoupon(RequestFixDiscountDto requestFixDiscountDto);
}
