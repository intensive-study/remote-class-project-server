package org.server.remoteclass.dto.order;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class RequestCancelPartialPurchaseDto {

    //구매 부분 취소할때 입력하는 dto
    @NotEmpty
    private List<RequestOrderLectureDto> orderLectures = new ArrayList<>();

}
