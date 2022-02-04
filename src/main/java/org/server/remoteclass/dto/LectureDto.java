package org.server.remoteclass.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.Category;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
@Builder
public class LectureDto {
    private Long lectureId;
    private String title;
    private String description;
    private Integer price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Category category;
    private User user;

    public LectureDto(Lecture lecture){
        this.lectureId = lecture.getLectureId();
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.price = lecture.getPrice();
        this.startDate = lecture.getStartDate();
        this.endDate = lecture.getEndDate();
        this.category = lecture.getCategory();
        this.user = lecture.getUser();
    }

    public static LectureDto from(Lecture lecture){
        if(lecture == null) return null;
        return LectureDto.builder()
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .price(lecture.getPrice())
                .startDate(lecture.getStartDate())
                .endDate(lecture.getEndDate())
                .category(lecture.getCategory())
                .user(lecture.getUser())
                .build();
    }
}