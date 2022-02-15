package org.server.remoteclass.dto.student;

import lombok.*;
import org.server.remoteclass.entity.Student;


@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class StudentDto {

    //수강생 조회시 사용하는 dto
    private Long studentId;
    private Long lectureId;
    private Long userId;

    public static StudentDto from(Student student){
        if(student == null) return null;
        return StudentDto.builder()
                .studentId(student.getStudentId())
                .lectureId(student.getLecture().getLectureId())
                .userId(student.getUser().getUserId())
                .build();
    }
}
