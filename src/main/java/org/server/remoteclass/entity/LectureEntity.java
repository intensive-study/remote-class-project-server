package org.server.remoteclass.entity;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.security.Timestamp;

@Data
@Entity
@Builder
@Table(name="lecture")
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class LectureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id", nullable=false)
    private Long lectureId;     //강의 아이디
    @Column(name = "title", nullable=false, length=50)
    private String title;       //강의제목
    @Column(name = "description")
    private String description;     //강의상세설명
    @Column(name = "price", nullable=false)
    private Integer price;      //수강료
//    @Column(name = "start_date")
//    private Timestamp startDate;        //강의 시작일
//    @Column(name = "end_date")
//    private Timestamp endDate;          //강의 종료일

//    @ManyToOne
//    @JoinColumn(name="category_id")
//    private CategoryEntity categoryEntity;       //강의 카테고리

//    public void updateLecture(LectureDto lectureDto){
//        this.lectureId = lectureDto.getLectureId();
//        this.title = lectureDto.getTitle();
//        this.description = lectureDto.getDescription();
//        this.price = lectureDto.getPrice();
//        this.startDate = lectureDto.getStartDate();
//        this.endDate = lectureDto.getEndDate();
//        this.categoryEntity = lectureDto.getCategoryEntity();
//    }
}
