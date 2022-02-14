package org.server.remoteclass.dto;

import lombok.*;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;


@Getter @Setter @NoArgsConstructor
@Builder
public class RequestOrderLectureDto {

    //강의 주문할때 입력하는 dto
    public RequestOrderLectureDto(Long lectureId) {
        this.lectureId = lectureId;
    }

    private Long lectureId;

}