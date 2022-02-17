package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("F")
public class FixDiscount extends Coupon{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long fixDiscountId;
    private int discountPrice;
}
