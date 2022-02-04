package org.server.remoteclass.dto;

import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.Category;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
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
}