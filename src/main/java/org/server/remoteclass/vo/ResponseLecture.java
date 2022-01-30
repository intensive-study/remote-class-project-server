package org.server.remoteclass.vo;

import org.server.remoteclass.entity.CategoryEntity;
import org.server.remoteclass.entity.LectureEntity;

import java.security.Timestamp;

public class ResponseLecture {
    private Long lectureId;
    private String title;
    private String description;
    private Integer price;
    private Timestamp startDate;
    private Timestamp endDate;
    private CategoryEntity categoryEntity;

    public ResponseLecture(LectureEntity lectureEntity){
        this.lectureId = lectureEntity.getLectureId();
//        this.categoryEntity = lectureEntity.getCategoryEntity();
        this.title = lectureEntity.getTitle();
        this.description = lectureEntity.getDescription();
        this.price = lectureEntity.getPrice();
//        this.startDate = lectureEntity.getStartDate();
//        this.endDate = lectureEntity.getEndDate();
    }
}
