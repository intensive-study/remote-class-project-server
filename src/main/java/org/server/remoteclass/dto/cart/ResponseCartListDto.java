package org.server.remoteclass.dto.cart;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class ResponseCartListDto {

    //장바구니안의 개별 강의 조회하는 dto
    private List<ResponseCartDto> responseCartDtoList = new ArrayList<>();
    private Integer sumCart;
    private Integer countCart;

}