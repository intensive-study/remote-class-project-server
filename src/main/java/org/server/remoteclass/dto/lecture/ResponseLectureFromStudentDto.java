package org.server.remoteclass.dto.lecture;

import lombok.*;
import org.server.remoteclass.entity.Student;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLectureFromStudentDto {

    // 학생 권한에서 조회 시에 사용하는 Dto
    private Long lectureId;
    private String title;
    private String description;
    private Integer price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String categoryName;
    private String lecturer;

    public ResponseLectureFromStudentDto(Student student) {
        this.lectureId = student.getLecture().getLectureId();
        this.title = student.getLecture().getTitle();
        this.description = student.getLecture().getDescription();
        this.price = student.getLecture().getPrice();
        this.startDate = student.getLecture().getStartDate();
        this.endDate = student.getLecture().getEndDate();
        this.categoryName = student.getLecture().getCategory().getCategoryName();
        this.lecturer = student.getLecture().getUser().getName();
    }

    public static ResponseLectureFromStudentDto from(Student student) {
        if (student == null) return null;
        return ResponseLectureFromStudentDto.builder()
                .lectureId(student.getLecture().getLectureId())
                .title(student.getLecture().getTitle())
                .description(student.getLecture().getDescription())
                .price(student.getLecture().getPrice())
                .startDate(LocalDateTime.from(student.getLecture().getStartDate()))
                .endDate(LocalDateTime.from(student.getLecture().getEndDate()))
                .categoryName(student.getLecture().getCategory().getCategoryName())
                .lecturer(student.getLecture().getUser().getName())
                .build();
    }
}