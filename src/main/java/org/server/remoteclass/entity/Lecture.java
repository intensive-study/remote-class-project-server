package org.server.remoteclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter @Setter
public class Lecture {
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
    @Column(name = "start_date")
    private LocalDateTime startDate;        //강의 시작일
    @Column(name = "end_date")
    private LocalDateTime endDate;          //강의 종료일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    @JsonIgnore
    private Category category;       //강의 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lecturer") //강의자
    @JsonIgnore
    private User user;


}