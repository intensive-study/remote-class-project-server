package org.server.remoteclass.dto.lecture;

import lombok.*;
import org.server.remoteclass.entity.Student;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLectureDto {

    // 학생 권한에서 조회 시에 사용하는 Dto
    private Long lectureId;
    private String title;
    private String description;
    private Integer price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long categoryId;
    private Long lecturer;

    public static ResponseLectureDto from(Student student){
        if(student == null) return null;
        return ResponseLectureDto.builder()
                .lectureId(student.getLecture().getLectureId())
                .title(student.getLecture().getTitle())
                .description(student.getLecture().getDescription())
                .price(student.getLecture().getPrice())
                .startDate(LocalDateTime.from(student.getLecture().getStartDate()))
                .endDate(LocalDateTime.from(student.getLecture().getEndDate()))
                .categoryId(student.getLecture().getCategory().getCategoryId())
                .lecturer(student.getLecture().getUser().getUserId())
                .build();
    }
}
