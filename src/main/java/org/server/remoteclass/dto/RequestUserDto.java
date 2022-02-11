package org.server.remoteclass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor
public class RequestUserDto {

    // 회원가입 시에 사용하는 Dto
    private String email;
    private String name;
    private String password;

    public RequestUserDto(){

    }

}
