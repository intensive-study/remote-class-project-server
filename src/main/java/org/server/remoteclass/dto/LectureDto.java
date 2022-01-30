package org.server.remoteclass.dto;

import java.security.Timestamp;

import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.CategoryEntity;
import org.server.remoteclass.entity.LectureEntity;

@Getter
@Setter
public class LectureDto {
    private Long lectureId;
    private String title;
    private String description;
    private Integer price;
//    private Timestamp startDate;
//    private Timestamp endDate;
//    private CategoryEntity categoryEntity;

    public LectureDto(LectureEntity lectureEntity){
        this.lectureId = lectureEntity.getLectureId();
        this.title = lectureEntity.getTitle();
        this.description = lectureEntity.getDescription();
        this.price = lectureEntity.getPrice();
//        this.startDate = lectureEntity.getStartDate();
//        this.endDate = lectureEntity.getEndDate();
//        this.categoryEntity = lectureEntity.getCategoryEntity();
    }
}
