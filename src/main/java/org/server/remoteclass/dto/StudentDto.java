package org.server.remoteclass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.User;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class StudentDto {
    private Long studentId;
    private Lecture lecture;
    private User user;

    public StudentDto(Student student){
        this.studentId = student.getStudentId();
        this.lecture = student.getLecture();
        this.user = student.getUser();
    }

    public static StudentDto from(Student student){
        if(student == null) return null;
        return StudentDto.builder()
                .lecture(student.getLecture())
                .user(student.getUser())
                .build();
    }
}
