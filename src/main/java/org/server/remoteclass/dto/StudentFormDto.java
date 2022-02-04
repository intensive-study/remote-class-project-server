package org.server.remoteclass.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class StudentFormDto {
    private Long studentId;
    @NotNull(message="수강 강좌 필수입력")
    private Long lectureId;
    private Long userId;
}