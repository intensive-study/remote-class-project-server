package org.server.remoteclass.dto.cart;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter @Setter @NoArgsConstructor
public class RequestCartDto {

    //장바구니에 입력하는 dto
    @NotNull
    @Min(1)
    private Long lectureId;
}