package org.server.remoteclass.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateUserDto {

    @NotEmpty
    @Size(max = 20)
    private String name;
    @NotEmpty
    @Size(min = 8, max = 20)
    private String password;
}
