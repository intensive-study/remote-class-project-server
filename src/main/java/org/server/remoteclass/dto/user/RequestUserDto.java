package org.server.remoteclass.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
@Builder
public class RequestUserDto {

    // 회원가입 시에 사용하는 Dto
    @NotEmpty
    @Size(min = 8, max = 30)
    private String email;
    @NotEmpty
    @Size(max = 20)
    private String name;
    @NotEmpty
    @Size(min = 8, max = 20)
    private String password;

    public RequestUserDto(){

    }

    public RequestUserDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
