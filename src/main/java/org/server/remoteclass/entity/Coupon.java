package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@DynamicInsert
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    private String couponCode;
    @Column(columnDefinition = "boolean default 0")
    private boolean couponUsed;
    @Column(name = "coupon_start_date")
    private LocalDate startDate;        //강의 시작일
    @Column(name = "coupon_end_date")
    private LocalDate endDate;

}
