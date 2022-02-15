package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Event {

    @Id
    @Column(name = "event_id")
    private Long eventId;

    private LocalDateTime eventStartDate;
    private LocalDateTime eventEndDate;
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
