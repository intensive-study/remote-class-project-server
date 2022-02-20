package org.server.remoteclass.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@Builder
public class RequestUserDto {

    // 회원가입 시에 사용하는 Dto
    @NotEmpty
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

    public RequestUserDto(){

    }

    public RequestUserDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
