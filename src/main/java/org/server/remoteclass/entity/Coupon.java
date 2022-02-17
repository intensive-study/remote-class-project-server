package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@EntityListeners(AuditingEntityListener.class) // 이 어노테이션이 있어야 @CreatedDate가 작동합니다.
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;
    private String couponCode;
    private String title;
    // 쿠폰 VALID 상태면 회원이 발급받을 수 있고, 아니면 불가능하게 하려 합니다.
    @Column(columnDefinition = "boolean default 1")
    private boolean couponValid;
    // 쿠폰 유효기간 -> 종료 날짜보다 유효일을 두는 게 맞다고 생각했어요. 각기 다른 회원이 다른 날짜에 쿠폰을 발급받을 수 있기 때문입니다.
    // 쿠폰 종료일은 두 회원이 달라야 하므로, 유효일을 두는 게 맞다고 생각했습니다.
    private int couponValidDays; // couponValidDays 중에 무엇이 적합한 지 생각중입니다.

    // 쿠폰 생성일입니다.
    @Column(name = "coupon_created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "coupon")
    private List<IssuedCoupon> issuedCouponList = new ArrayList<>();



    //FisDiscount에 있는 컬럼
//    private Long fixDiscountId;
//    private int discountPrice;
    //RateDiscount에 있는 컬럼
//    private Long rateDiscountId;
//    private int discountRate;
}
