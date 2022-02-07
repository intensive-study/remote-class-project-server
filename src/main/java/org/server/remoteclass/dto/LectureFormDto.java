package org.server.remoteclass.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LectureFormDto {

    private Long lectureId;
    @NotNull(message="강의명 필수입력")
    private String title;
    private String description;
    @NotNull(message="수강료 필수입력")
    private Integer price;
    @NotNull(message="수강시작 : yyyy-MM-dd")
    private LocalDateTime startDate;
    @NotNull(message="수강종료 : yyyy-MM-dd")
    private LocalDateTime endDate;
    @NotNull(message="카테고리 선택, 숫자로 입력")
    private Long categoryId;
    private Long userId;
}