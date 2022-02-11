package org.server.remoteclass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.entity.User;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@AllArgsConstructor
public class ResponseUserDto {

    // 사용자가 다른 사용자 조회 시에 보일 정보
    // 비밀번호 뿐 아니라 이메일과 같이 개인 신상을 알 수 없게 최소한의 정보만 제공하는 게 맞다고 생각합니다.
    private Long userId;
    private String name;
    private UserRole userRole;

    public ResponseUserDto(){

    }

    public static ResponseUserDto from(User user){
        if(user == null) return null;
        return ResponseUserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .userRole(user.getUserRole())
                .build();
    }
}
