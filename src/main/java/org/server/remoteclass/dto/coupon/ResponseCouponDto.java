package org.server.remoteclass.dto.coupon;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.server.remoteclass.dto.issuedcoupon.ResponseIssuedCouponDto;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.FixDiscount;
import org.server.remoteclass.entity.RateDiscount;

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
    private boolean couponValid;
    private int couponValidDays;
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
                .couponValid(coupon.isCouponValid())
                .couponValidDays(coupon.getCouponValidDays())
                .createdDate(coupon.getCreatedDate())
                .issuedCouponList(coupon.getIssuedCouponList().stream()
                        .map(issuedCoupon ->
                                        new ResponseIssuedCouponDto(
                                                issuedCoupon.getIssuedCouponId(),
                                                issuedCoupon.isCouponUsed(),
                                                issuedCoupon.getCoupon().getCouponCode(),
                                                issuedCoupon.getCouponValidDate())
                        ).collect(Collectors.toList()))
                .build();
    }

    public static ResponseCouponDto from(FixDiscount fixDiscount){
        if(fixDiscount == null) return null;
        return ResponseCouponDto.builder()
                .couponId(fixDiscount.getCouponId())
                .couponCode(fixDiscount.getCouponCode())
                .couponValid(fixDiscount.isCouponValid())
                .couponValidDays(fixDiscount.getCouponValidDays())
                .title(fixDiscount.getTitle())
                .discountPrice(fixDiscount.getDiscountPrice())
                .createdDate(fixDiscount.getCreatedDate())
                .build();
    }

    public static ResponseCouponDto from(RateDiscount rateDiscount){
        if(rateDiscount == null) return null;
        return ResponseCouponDto.builder()
                .couponId(rateDiscount.getCouponId())
                .couponCode(rateDiscount.getCouponCode())
                .couponValid(rateDiscount.isCouponValid())
                .couponValidDays(rateDiscount.getCouponValidDays())
                .createdDate(rateDiscount.getCreatedDate())
                .title(rateDiscount.getTitle())
                .discountRate(rateDiscount.getDiscountRate())
                .build();
    }
}
