package org.server.remoteclass.dto.issuedcoupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.IssuedCoupon;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class IssuedCouponDto {

    private Long issuedCouponId;
    private Long userId;
    private Long couponId;
    private boolean couponUsed;
    private String couponCode;
    private LocalDateTime couponValidDate; // 쿠폰 유효기간이라 봐주시면 됩니다.

    public IssuedCouponDto(){
    }

    public static IssuedCouponDto from(IssuedCoupon issuedCoupon){
        if(issuedCoupon == null) return null;
        return IssuedCouponDto.builder()
                .issuedCouponId(issuedCoupon.getIssuedCouponId())
                .userId(issuedCoupon.getUser().getUserId())
                .couponId(issuedCoupon.getCoupon().getCouponId())
                .couponCode(issuedCoupon.getCoupon().getCouponCode())
                .couponUsed(issuedCoupon.isCouponUsed())
                .couponValidDate(issuedCoupon.getCouponValidDate())
                .build();
        // 쿠폰 종류를 식별할 수 있는 컬럼 필요.
    }
}
