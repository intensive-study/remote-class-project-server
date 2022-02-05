package org.server.remoteclass.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.User;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CouponDto {

    private Long couponId;
    private String couponCode;
    private boolean couponUsed;
    private int couponValidTime;
    private LocalDate startDate;

    public CouponDto(){

    }

    public static CouponDto from(Coupon coupon){
        if(coupon == null) return null;
        return CouponDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .couponUsed(coupon.isCouponUsed())
                .couponValidTime(coupon.getCouponValidTime())
                .startDate(coupon.getStartDate())
                .build();
    }
}
