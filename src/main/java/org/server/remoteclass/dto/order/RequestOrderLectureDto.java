package org.server.remoteclass.dto.order;

import lombok.*;
import org.server.remoteclass.entity.OrderLecture;

@Getter @Setter @NoArgsConstructor
public class RequestOrderLectureDto {

    //강의 주문할때 입력하는 dto
    private Long lectureId;

    public RequestOrderLectureDto(OrderLecture orderLecture) {
        this.lectureId = orderLecture.getLecture().getLectureId();
    }
}