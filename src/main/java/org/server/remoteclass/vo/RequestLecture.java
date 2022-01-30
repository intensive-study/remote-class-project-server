package org.server.remoteclass.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.entity.CategoryEntity;

import javax.validation.constraints.NotNull;
import java.security.Timestamp;

@Getter
@Setter
@Builder
public class RequestLecture {

    private Long lectureId;
    @NotNull(message="강의명 필수입력")
    private String title;
    private String description;
    @NotNull(message="수강료 필수입력")
    private Integer price;
//    private Timestamp startDate;
//    private Timestamp endDate;
//    @NotNull(message="카테고리 선택")
//    private Long categoryId;
}
