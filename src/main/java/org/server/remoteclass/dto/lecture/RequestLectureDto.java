package org.server.remoteclass.dto.lecture;

import lombok.*;
import org.server.remoteclass.entity.Lecture;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class RequestLectureDto {

    // 강의 생성에 사용하는 Dto
    private String title;
    private String description;
    private Integer price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long categoryId;
    private Long lecturer;

    public static RequestLectureDto from(Lecture lecture){
        if(lecture == null) return null;
        return RequestLectureDto.builder()
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .price(lecture.getPrice())
                .startDate(LocalDateTime.from(lecture.getStartDate()))
                .endDate(LocalDateTime.from(lecture.getEndDate()))
                .categoryId(lecture.getCategory().getCategoryId())
                .lecturer(lecture.getUser().getUserId())
                .build();
    }
}