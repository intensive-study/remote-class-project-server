package org.server.remoteclass.dto.lecture;

import lombok.*;
import org.server.remoteclass.entity.Lecture;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class ResponseLectureDto {

    // 강의 조회에 사용하는 Dto
    private Long lectureId;
    private String title;
    private String description;
    private Integer price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long categoryId;
    private String categoryName;
    private Long lecturer;
    private String lecturerName;

    public static ResponseLectureDto from(Lecture lecture){
        if(lecture == null) return null;
        return ResponseLectureDto.builder()
                .lectureId(lecture.getLectureId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .price(lecture.getPrice())
                .startDate(LocalDateTime.from(lecture.getStartDate()))
                .endDate(LocalDateTime.from(lecture.getEndDate()))
                .categoryId(lecture.getCategory().getCategoryId())
                .categoryName(lecture.getCategory().getCategoryName())
                .lecturer(lecture.getUser().getUserId())
                .lecturerName(lecture.getUser().getName())
                .build();
    }
}