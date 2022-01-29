package org.server.remoteclass.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.remoteclass.entity.UserEntity;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Timestamp registerDate;
    private Set<AuthorityDto> authorityDtoSet;

    public static UserDto from(UserEntity user){
        if(user == null) return null;
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .registerDate(user.getRegisterDate())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
