package org.server.remoteclass.dto;

import lombok.*;
import org.server.remoteclass.entity.Student;


@Getter
@Setter
public class RequestStudentDto {

    //수강 신청시 사용하는 dto
    private Long lectureId;

    public RequestStudentDto(){

    }


    public RequestStudentDto(Long lectureId){
        this.lectureId = lectureId;
    }
//    public static RequestStudentDto from(Student student){
//        if(student == null) return null;
//        return RequestStudentDto.builder()
//                .lectureId(student.getLecture().getLectureId())
//                .userId(student.getUser().getUserId())
//                .build();
//    }
}
