package org.server.remoteclass.service.fixDiscount;

import org.server.remoteclass.dto.fixDiscount.RequestFixDiscountDto;
import org.server.remoteclass.dto.fixDiscount.ResponseFixDiscountDto;

import java.util.List;

public interface FixDiscountService {
    //정률할인 쿠폰 생성
    void createFixDiscountCoupon(RequestFixDiscountDto requestFixDiscountDto);
    List<ResponseFixDiscountDto> getAllFixDiscountCoupons();
    ResponseFixDiscountDto getFixDiscountCoupon(Long couponId);
}
