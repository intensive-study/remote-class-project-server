package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;

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
    private int couponValidTime;
    @Column(name = "coupon_start_date")
    @CreatedDate
    private LocalDate startDate;        //쿠폰 생성일

}
