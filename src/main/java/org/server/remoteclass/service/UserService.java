package org.server.remoteclass.service;

import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    UserDto signup(UserDto userDto);
    UserDto getUserWithAuthorities(String email);
    Optional<UserEntity> getMyUserWithAuthorities();
    Iterable<UserEntity> getUsersByAll();
}
