package org.server.remoteclass.dto.student;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class RequestStudentDto {

    //수강 신청시 사용하는 dto
    @NotNull
    @Min(1)
    private Long lectureId;

}
