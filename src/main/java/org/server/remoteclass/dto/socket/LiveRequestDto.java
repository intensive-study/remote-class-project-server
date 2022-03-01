package org.server.remoteclass.dto.socket;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;

@Builder
public class LiveRequestDto {

    @NotEmpty
    public String type;

    @NotEmpty
    public Long userId;

    @NotEmpty
    public Long lectureId;

    public String token;

    public SdpDto sdp;

    public IceCandidateDto candidate;

    public static LiveRequestDto buildBasicDto(String type, Long userId, Long lectureId, String token) {
        LiveRequestDtoBuilder liveRequestDtoBuilder = LiveRequestDto
                .builder()
                .type(type)
                .userId(userId)
                .lectureId(lectureId);

        if(token != null) liveRequestDtoBuilder.token(token);
        return liveRequestDtoBuilder
                .build();

    }

}
