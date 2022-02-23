package org.server.remoteclass.dto.lecture;

import lombok.*;
import org.server.remoteclass.entity.Lecture;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class RequestModifyLectureDto {

    // 강의 수정에 사용하는 Dto
    @NotNull
    @Min(1)
    private Long lectureId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotNull
    @Min(0)
    private Integer price;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    @Min(1)
    private Long categoryId;

    public static RequestModifyLectureDto from(Lecture lecture){
        if(lecture == null) return null;
        return RequestModifyLectureDto.builder()
                .lectureId(lecture.getLectureId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .price(lecture.getPrice())
                .startDate(LocalDateTime.from(lecture.getStartDate()))
                .endDate(LocalDateTime.from(lecture.getEndDate()))
                .categoryId(lecture.getCategory().getCategoryId())
                .build();
    }
}