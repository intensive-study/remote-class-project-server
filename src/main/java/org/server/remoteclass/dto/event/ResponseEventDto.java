package org.server.remoteclass.dto.event;

import lombok.*;
import org.server.remoteclass.entity.Event;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEventDto {

    private Long eventId;
    private Long couponId;
    private String title;
    private LocalDateTime eventStartDate;
    private LocalDateTime eventEndDate;
    private LocalDateTime eventCreatedDate;

    public static ResponseEventDto from(Event event){
        if(event == null) return null;
        return ResponseEventDto.builder()
                .eventId(event.getEventId())
                .couponId(event.getCoupon().getCouponId())
                .title(event.getTitle())
                .eventStartDate(event.getEventStartDate())
                .eventEndDate(event.getEventEndDate())
                .build();
    }
}
