package org.server.remoteclass.dto.order;

import lombok.*;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;


@Getter @Setter @NoArgsConstructor  @AllArgsConstructor
@Builder
public class OrderLectureDto {

    private Long orderLectureId;
    private Lecture lecture;
    private Order order;


    public static OrderLectureDto from(OrderLecture orderLecture){
        if(orderLecture==null) return null;
        return OrderLectureDto.builder()
                .orderLectureId(orderLecture.getOrderLectureId())
                .lecture(orderLecture.getLecture())
                .order(orderLecture.getOrder())
                .build();
    }
}
