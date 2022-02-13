package org.server.remoteclass.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.remoteclass.dto.auth.AuthorityDto;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.constant.UserRole;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long userId;
    private String email;
    private String name;
    private String password;
    private UserRole userRole;
    private LocalDateTime registerDate;
    private Set<AuthorityDto> authorityDtoSet;

    public UserDto(String email, String name, String password){
        this.email = email;
        this.name = name;
        this.password = password;
    }

    // 본인이 본인 정보 조회 or 관리자가 조회 시 사용할 함수
    public static UserDto from(User user){
        if(user == null) return null;
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .registerDate(user.getRegisterDate())
                .userRole(user.getUserRole())
                .build();
    }

}
