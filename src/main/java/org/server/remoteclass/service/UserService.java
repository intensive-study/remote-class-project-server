package org.server.remoteclass.service;

import org.server.remoteclass.dto.LoginDto;
import org.server.remoteclass.dto.TokenDto;
import org.server.remoteclass.dto.TokenRequestDto;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    UserDto signup(UserDto userDto);
    TokenDto login(LoginDto loginDto);
    TokenDto reissue(TokenRequestDto tokenRequestDto);
    UserDto getUserWithAuthorities(String email);
    Optional<UserEntity> getMyUserWithAuthorities();
    Iterable<UserEntity> getUsersByAll();
}
