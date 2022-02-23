package org.server.remoteclass.dto.cart;

import lombok.*;
import org.server.remoteclass.entity.Cart;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class ResponseCartDto {

    //장바구니안의 강의 조회하는 dto
    private Long id;
    private Long lectureId;
    private String title;
    private String lecturer;
    private Integer price;
    private LocalDateTime createdDate;


    public static ResponseCartDto from(Cart cart) {
        if(cart == null) return null;
        return ResponseCartDto.builder()
                .id(cart.getId())
                .lectureId(cart.getLecture().getLectureId())
                .title(cart.getLecture().getTitle())
                .lecturer(cart.getLecture().getUser().getName())
                .price(cart.getLecture().getPrice())
                .createdDate(cart.getCreatedDate())
                .build();
    }

}