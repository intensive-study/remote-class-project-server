package org.server.remoteclass.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.Coupon;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CouponDto {

    private Long couponId;
    private String couponCode;
    private boolean couponValid;
    private int couponValidDays;
    private LocalDateTime createdDate;
    private LocalDateTime endDate;

    public CouponDto(){

    }

    public CouponDto(int couponValidDays, LocalDateTime endDate){
        this.couponValidDays = couponValidDays;
        this.endDate = endDate;
    }

    public static CouponDto from(Coupon coupon){
        if(coupon == null) return null;
        return CouponDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .couponValid(coupon.isCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCratedDate())
                .endDate(coupon.getEndDate())
                .build();
    }
}
