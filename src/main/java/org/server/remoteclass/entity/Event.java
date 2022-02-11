package org.server.remoteclass.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Event {

    @javax.persistence.Id
    @Column(name = "event_id")
    private Long Id;

    private Long couponId;
    private LocalDateTime eventStartDate;
    private LocalDateTime eventEndDate;
    private String title;

}
