package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("R")
public class RateDiscount extends Coupon {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long rateDiscountId;
    private int discountRate;
}
