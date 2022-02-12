package org.server.remoteclass.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.server.remoteclass.entity.Event;

import java.time.LocalDateTime;

@Getter
@Builder
public class RequestEventDto {

    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public RequestEventDto(){

    }

    public RequestEventDto(String title, LocalDateTime startDate, LocalDateTime endDate){
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static RequestEventDto from(Event event){
        if(event == null) return null;
        return RequestEventDto.builder()
                .title(event.getTitle())
                .startDate(event.getEventStartDate())
                .endDate(event.getEventEndDate())
                .build();
    }
}
