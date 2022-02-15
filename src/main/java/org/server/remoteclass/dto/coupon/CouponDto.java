package org.server.remoteclass.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.Coupon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public CouponDto(){

    }

    public CouponDto(int couponValidDays){
        this.couponValidDays = couponValidDays;
    }

    public static CouponDto from(Coupon coupon){
        if(coupon == null) return null;
        return CouponDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .couponValid(coupon.isCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCreatedDate())
                .build();
    }
}
