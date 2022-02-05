package org.server.remoteclass.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    private String couponCode;
    private boolean couponUsed;
    @Column(name = "coupon_start_date")
    private LocalDate startDate;        //강의 시작일
    @Column(name = "coupon_end_date")
    private LocalDate endDate;

}
