package org.server.remoteclass.entity;

import javax.persistence.*;

@Entity
public class FixDiscount {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    private Coupon coupon;

    private String title;
    private int discountPrice;
}
