package org.server.remoteclass.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.remoteclass.entity.Student;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseStudentByLecturerDto {

    // 강의자 권한에서 수강생 조회 시에 사용하는 Dto
    private Long userId;
    private String email;
    private String name;

    public static ResponseStudentByLecturerDto from(Student student){
        if(student == null) return null;
        return ResponseStudentByLecturerDto.builder()
                .email(student.getUser().getEmail())
                .userId(student.getUser().getUserId())
                .name(student.getUser().getName())
                .build();
    }
}
