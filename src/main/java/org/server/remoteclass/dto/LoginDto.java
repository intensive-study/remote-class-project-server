package org.server.remoteclass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    private String name;

    @NotNull
    @Size(min=8, max=50)
    private String email;

    @NotNull
    @Size(min=8, max=100)
    private String password;
}
