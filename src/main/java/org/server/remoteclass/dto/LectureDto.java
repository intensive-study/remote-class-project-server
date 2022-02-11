package org.server.remoteclass.dto;

import lombok.*;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class LectureDto {
    private Long lectureId;
    private String title;
    private String description;
    private Integer price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long categoryId;
    private Long lecturer;

    public static LectureDto from(Lecture lecture){
        if(lecture == null) return null;
        return LectureDto.builder()
                .lectureId(lecture.getLectureId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .price(lecture.getPrice())
                .startDate(LocalDateTime.from(lecture.getStartDate()))
                .endDate(LocalDateTime.from(lecture.getEndDate()))
                .categoryId(lecture.getCategory().getCategoryId())
                .lecturer(lecture.getUser().getUserId())
                .build();
    }

    public static LectureDto from(Student student){
        if(student == null) return null;
        return LectureDto.builder()
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