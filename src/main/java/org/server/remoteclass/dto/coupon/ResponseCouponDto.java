package org.server.remoteclass.dto.coupon;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.server.remoteclass.dto.issuedcoupon.ResponseIssuedCouponDto;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.FixDiscountCoupon;
import org.server.remoteclass.entity.RateDiscountCoupon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCouponDto {

    private Long couponId;
    private String couponCode;
    private Boolean couponValid;
    private Integer couponValidDays;
    private LocalDateTime createdDate;
    private List<ResponseIssuedCouponDto> issuedCouponList = new ArrayList<>();

    //추가된 부분
    private String title;
    private Integer discountRate;
    private Integer discountPrice;

    public static ResponseCouponDto from(Coupon coupon){
        if(coupon == null) return null;
        return ResponseCouponDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .title(coupon.getTitle())
                .couponValid(coupon.getCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCreatedDate())
                .issuedCouponList(coupon.getIssuedCouponList().stream()
                        .map(ResponseIssuedCouponDto::NotIncludeCoupon
                        ).collect(Collectors.toList()))
                .build();
    }

    public static ResponseCouponDto notIncludeIssuedCoupons(Coupon coupon){
        if(coupon == null) return null;
        return ResponseCouponDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .title(coupon.getTitle())
                .couponValid(coupon.getCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCreatedDate())
                .build();
    }

    public static ResponseCouponDto from(FixDiscountCoupon fixDiscountCoupon){
        if(fixDiscountCoupon == null) return null;
        return ResponseCouponDto.builder()
                .couponId(fixDiscountCoupon.getCouponId())
                .couponCode(fixDiscountCoupon.getCouponCode())
                .couponValid(fixDiscountCoupon.getCouponValid())
                .couponValidDays(fixDiscountCoupon.getCouponValidDays())
                .title(fixDiscountCoupon.getTitle())
                .discountPrice(fixDiscountCoupon.getDiscountPrice())
                .createdDate(fixDiscountCoupon.getCreatedDate())
                .build();
    }

    public static ResponseCouponDto from(RateDiscountCoupon rateDiscountCoupon){
        if(rateDiscountCoupon == null) return null;
        return ResponseCouponDto.builder()
                .couponId(rateDiscountCoupon.getCouponId())
                .couponCode(rateDiscountCoupon.getCouponCode())
                .couponValid(rateDiscountCoupon.getCouponValid())
                .couponValidDays(rateDiscountCoupon.getCouponValidDays())
                .createdDate(rateDiscountCoupon.getCreatedDate())
                .title(rateDiscountCoupon.getTitle())
                .discountRate(rateDiscountCoupon.getDiscountRate())
                .build();
    }
}
