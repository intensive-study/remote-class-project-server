package org.server.remoteclass.dto;

import lombok.*;
import org.server.remoteclass.entity.OrderLecture;


@Getter @Setter @NoArgsConstructor
public class ResponseOrderLectureDto {

    //주문된 강의 출력할때 사용하는 dto
    private Long lectureId;
    private Long orderId;

    public ResponseOrderLectureDto(OrderLecture orderLecture) {
        this.lectureId = orderLecture.getLecture().getLectureId();
        this.orderId = orderLecture.getOrder().getOrderId();
    }
}
