package org.server.remoteclass.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
@Builder
public class RequestUserDto {

    // 회원가입 시에 사용하는 Dto
    @NotEmpty
    @Range(min = 8, max = 30)
    private String email;
    @NotEmpty
    @Range(max = 20)
    private String name;
    @NotEmpty
    @Range(min = 8, max = 20)
    private String password;

    public RequestUserDto(){

    }

    public RequestUserDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
