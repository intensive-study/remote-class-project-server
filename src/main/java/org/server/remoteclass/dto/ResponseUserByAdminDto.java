package org.server.remoteclass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.entity.User;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponseUserByAdminDto {

    private Long userId;
    private String email;
    private String name;
    private String password;
    private UserRole userRole;
    private LocalDateTime registerDate;
    private Set<AuthorityDto> authorityDtoSet;

    public ResponseUserByAdminDto(){

    }

    public ResponseUserByAdminDto(String email, String name, String password){
        this.email = email;
        this.name = name;
        this.password = password;
    }

    // 관리자 혹은 본인이 조회 시 사용하는 함수
    public static ResponseUserByAdminDto from(User user){
        if(user == null) return null;
        return ResponseUserByAdminDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .registerDate(user.getRegisterDate())
                .userRole(user.getUserRole())
                .build();
    }
}
