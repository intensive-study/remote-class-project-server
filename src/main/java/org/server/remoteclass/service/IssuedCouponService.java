package org.server.remoteclass.service;

import org.server.remoteclass.dto.CouponDto;
import org.server.remoteclass.dto.IssuedCouponDto;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface IssuedCouponService {

    //쿠폰 발급받기
    IssuedCouponDto issueCoupon(String couponCode);
    //내가 발급받은 모든 쿠폰 조회
    List<IssuedCouponDto> getAllMyCoupons() throws IdNotExistException;
}
