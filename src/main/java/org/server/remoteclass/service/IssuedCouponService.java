package org.server.remoteclass.service;

import org.server.remoteclass.dto.coupon.IssuedCouponDto;
import org.server.remoteclass.dto.coupon.RequestIssuedCouponDto;
import org.server.remoteclass.dto.coupon.ResponseIssuedCouponDto;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface IssuedCouponService {

    //쿠폰 발급받기
    void issueCoupon(RequestIssuedCouponDto requestIssuedCouponDto) throws IdNotExistException;
    //내가 발급받은 모든 쿠폰 조회
    List<ResponseIssuedCouponDto> getAllMyCoupons() throws IdNotExistException;
    //발급쿠폰번호로 쿠폰 조회(내가 발급받은 쿠폰 상세조회)
    ResponseIssuedCouponDto getMyCoupon(Long issuedCouponId) throws IdNotExistException;

}
