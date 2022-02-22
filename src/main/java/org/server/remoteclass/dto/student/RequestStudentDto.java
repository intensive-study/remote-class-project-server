package org.server.remoteclass.dto.student;

import lombok.*;
import org.server.remoteclass.entity.Student;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class RequestStudentDto {

    //수강 신청시 사용하는 dto
    @NotNull
    @Min(1)
    private Long lectureId;

    public RequestStudentDto(){

    }

    public RequestStudentDto(Long lectureId){
        this.lectureId = lectureId;
    }

}
