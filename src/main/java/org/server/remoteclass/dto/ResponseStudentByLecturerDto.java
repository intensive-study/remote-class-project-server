package org.server.remoteclass.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.constant.UserRole;

import java.time.LocalDateTime;
import java.util.Set;

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
    private String password;
    private UserRole userRole;
    private LocalDateTime registerDate;
    private Set<AuthorityDto> authorityDtoSet;

    public static ResponseStudentByLecturerDto from(Student student){
        if(student == null) return null;
        return ResponseStudentByLecturerDto.builder()
                .email(student.getUser().getEmail())
                .userId(student.getUser().getUserId())
                .name(student.getUser().getName())
                .build();
    }
}
