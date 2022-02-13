package org.server.remoteclass.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RequestEventDto {

    //이벤트 관련 정보
    private String title;
    private LocalDateTime eventStartDate;
    private LocalDateTime eventEndDate;

    //쿠폰 관련 정보
    private int couponValidDays;

    public RequestEventDto(){

    }

}
