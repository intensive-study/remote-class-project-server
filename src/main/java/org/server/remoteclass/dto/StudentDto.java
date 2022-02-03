package org.server.remoteclass.dto;

import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.UserEntity;


@Getter
@Setter
public class StudentDto {
    private Long studentId;
    private Lecture lecture;
    private UserEntity user;

    public StudentDto(Student student){
        this.studentId = student.getStudentId();
        this.lecture = student.getLecture();
        this.user = student.getUser();
    }
}
