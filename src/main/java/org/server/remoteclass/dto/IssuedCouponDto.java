package org.server.remoteclass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.IssuedCoupon;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class IssuedCouponDto {

    private Long issuedCouponId;
    private Long userId;
    private Long couponId;
    private boolean couponUsed;
    private LocalDate couponEndDate;
    private String title;

    public IssuedCouponDto(){
    }

    public static IssuedCouponDto from(IssuedCoupon issuedCoupon){
        if(issuedCoupon == null) return null;
        return IssuedCouponDto.builder()
                .issuedCouponId(issuedCoupon.getIssuedCouponId())
                .userId(issuedCoupon.getUser().getUserId())
                .couponId(issuedCoupon.getCoupon().getCouponId())
                .couponUsed(issuedCoupon.isCouponUsed())
                .couponEndDate(issuedCoupon.getEndDate())
                .build();
        // 쿠폰 종류를 식별할 수 있는 컬럼 필요.
    }
}
