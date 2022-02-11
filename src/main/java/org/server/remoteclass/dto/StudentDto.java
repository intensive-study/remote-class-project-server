package org.server.remoteclass.dto;

import lombok.*;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.User;


@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class StudentDto {
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
