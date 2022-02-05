package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class RateDiscount {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    private Coupon coupon;

    private String title;
    private int discountRate;
}
