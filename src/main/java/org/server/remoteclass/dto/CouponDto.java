package org.server.remoteclass.dto;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CouponDto {

    private Long couponId;
    private String couponCode;
    private boolean couponValid;
    private int couponValidTime;
    private LocalDate createdDate;

    public CouponDto(){

    }

    public static CouponDto from(Coupon coupon){
        if(coupon == null) return null;
        return CouponDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .couponValid(coupon.isCouponValid())
                .couponValidTime(coupon.getCouponValidTime())
                .createdDate(coupon.getCratedDate())
                .build();
    }
}
