package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class FixDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fixDiscountId;

    @OneToOne(fetch = FetchType.LAZY)
    private Coupon coupon;

    private String title;
    private int discountPrice;
}
