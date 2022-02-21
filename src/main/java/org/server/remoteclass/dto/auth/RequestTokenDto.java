package org.server.remoteclass.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class RequestTokenDto {
    @NotEmpty
    private String accessToken;
    @NotEmpty
    private String refreshToken;
}
