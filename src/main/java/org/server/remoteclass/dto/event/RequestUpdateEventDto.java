package org.server.remoteclass.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateEventDto {

    //이벤트 관련 정보
    @NotNull
    @Min(1)
    private Long eventId;
    @NotEmpty
    @Size(min = 4, max = 40)
    private String title;
    @NotNull
    private LocalDateTime eventStartDate;
    @NotNull
    private LocalDateTime eventEndDate;

    //쿠폰 관련 정보
    @NotNull
    @Min(0)
    private Integer couponValidDays;
}
