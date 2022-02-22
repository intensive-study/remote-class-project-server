package org.server.remoteclass.dto.order;

import lombok.*;
import org.server.remoteclass.entity.OrderLecture;

import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor
public class RequestOrderLectureDto {

    //강의 주문할때 입력하는 dto
    @NotNull
    private Long lectureId;

    public RequestOrderLectureDto(OrderLecture orderLecture) {
        this.lectureId = orderLecture.getLecture().getLectureId();
    }
}