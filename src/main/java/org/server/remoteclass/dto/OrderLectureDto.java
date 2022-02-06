package org.server.remoteclass.dto;

import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;


@Getter @Setter
public class OrderLectureDto {

    private Long id;
    private Lecture lecture;
//    private Order order;

    public OrderLectureDto(){}

    public OrderLectureDto(OrderLecture orderLecture){
        this.id = orderLecture.getId();
        this.lecture = orderLecture.getLecture();
//        this.order = orderLecture.getOrder();
    }
}
