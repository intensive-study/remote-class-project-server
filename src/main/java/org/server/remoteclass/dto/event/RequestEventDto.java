package org.server.remoteclass.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.server.remoteclass.entity.Event;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RequestEventDto {

    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public RequestEventDto(){

    }

}
