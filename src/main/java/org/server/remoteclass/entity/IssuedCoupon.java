package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issuedCouponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    // 쿠폰 사용 여부를 나타냅니다
    @Column(columnDefinition = "boolean default 0")
    private Boolean couponUsed;

    //쿠폰 종료일입니다. 인프런은 7일의 기간이 주어지는 쿠폰이라면 정확히 7일을 주는 것이 아닌, 7일이 되는 날 23시 59분 59초까지 유효하게 설정합니다.
    //그런 의미에서 년, 월, 일까지 나오는 LocalDate형이 적합하다고 생각했습니다.
    @Column(name = "coupon_valid_date")
    private LocalDateTime couponValidDate;        //쿠폰 사용 가능한 날짜
}
