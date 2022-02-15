package org.server.remoteclass.dto.order;

import lombok.*;
import org.server.remoteclass.entity.OrderLecture;

@Getter @Setter @NoArgsConstructor
@Builder
public class RequestOrderLectureDto {

    //강의 주문할때 입력하는 dto
    public RequestOrderLectureDto(Long lectureId) {
        this.lectureId = lectureId;
    }

    private Long lectureId;

    public RequestOrderLectureDto(OrderLecture orderLecture) {
        this.lectureId = orderLecture.getOrderLectureId();
    }
}