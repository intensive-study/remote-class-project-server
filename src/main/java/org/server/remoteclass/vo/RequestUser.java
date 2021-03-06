package org.server.remoteclass.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {

    @NotNull(message = "이메일은 null이 될 수 없습니다.")
    @Size(min=8, message = "이메일은 최소 8자 이상입니다.")
    private String email;

    @NotNull(message = "이름은 null이 될 수 없습니다.")
    @Size(max=10, message = "이름은 최대 10글자까지 가능합니다.")
    private String name;

    @NotNull(message = "비밀번호는 null값이 될 수 없습니다.")
    @Size(min = 8, message = "비밀번호를 최소 8글자 이상 입력해 주세요.")
    private String password;
}
