package org.server.remoteclass.dto.cart;

import lombok.*;
import org.server.remoteclass.entity.OrderLecture;


@Getter @Setter @NoArgsConstructor
@Builder
public class RequestCartDto {

    //장바구니에 입력하는 dto
    private Long lectureId;

    public RequestCartDto(OrderLecture orderLecture) {
        this.lectureId = orderLecture.getLecture().getLectureId();
    }

    public RequestCartDto(Long lectureId){
        this.lectureId = lectureId;
    }
}