package org.server.remoteclass.dto.cart;

import lombok.*;

import org.server.remoteclass.entity.*;

import java.time.LocalDateTime;


@Getter @Setter @NoArgsConstructor  @AllArgsConstructor
@Builder
public class CartDto {

    private Long id;
    private Lecture lecture;
    private User user;
    private LocalDateTime createdDate;

    public static CartDto from(Cart cart){
        if(cart==null) return null;
        return CartDto.builder()
                .id(cart.getId())
                .lecture(cart.getLecture())
                .user(cart.getUser())
                .createdDate(cart.getCreatedDate())
                .build();
    }
}
