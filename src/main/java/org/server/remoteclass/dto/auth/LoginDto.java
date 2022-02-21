package org.server.remoteclass.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotEmpty
    @Size(min = 8, max=30)
    private String email;

    @NotEmpty
    @Size(min=8, max=20)
    private String password;
}
