package org.server.remoteclass.service.issuedCoupon;

import org.server.remoteclass.dto.issuedcoupon.RequestIssuedCouponDto;
import org.server.remoteclass.dto.issuedcoupon.ResponseIssuedCouponDto;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface IssuedCouponService {

    //쿠폰 발급받기
    void issueCoupon(RequestIssuedCouponDto requestIssuedCouponDto);
    //내가 발급받은 모든 쿠폰 조회
    List<ResponseIssuedCouponDto> getAllMyCoupons();
    //발급쿠폰번호로 쿠폰 조회(내가 발급받은 쿠폰 상세조회)
    ResponseIssuedCouponDto getMyCoupon(Long issuedCouponId);

}
