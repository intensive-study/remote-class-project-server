package org.server.remoteclass.dto.issuedcoupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.entity.IssuedCoupon;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponseIssuedCouponDto {

    private Long issuedCouponId;
    private Boolean couponUsed;
    private ResponseCouponDto coupon;
    private LocalDateTime couponValidDate; // 쿠폰 유효기간이라 봐주시면 됩니다.

    public ResponseIssuedCouponDto(){
    }

    public static ResponseIssuedCouponDto from(IssuedCoupon issuedCoupon){
        if(issuedCoupon == null) return null;
        return ResponseIssuedCouponDto.builder()
                .issuedCouponId(issuedCoupon.getIssuedCouponId())
                .coupon(ResponseCouponDto.notIncludeIssuedCoupons(issuedCoupon.getCoupon()))
                .couponUsed(issuedCoupon.isCouponUsed())
                .couponValidDate(issuedCoupon.getCouponValidDate())
                .build();
        // 쿠폰 종류를 식별할 수 있는 컬럼 필요.
    }

    public static ResponseIssuedCouponDto NotIncludeCoupon(IssuedCoupon issuedCoupon){
        if(issuedCoupon == null) return null;
        return ResponseIssuedCouponDto.builder()
                .issuedCouponId(issuedCoupon.getIssuedCouponId())
                .couponUsed(issuedCoupon.isCouponUsed())
                .couponValidDate(issuedCoupon.getCouponValidDate())
                .build();
    }
}
