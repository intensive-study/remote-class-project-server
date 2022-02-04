package org.server.remoteclass.service;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.User;

import java.util.Optional;

public interface UserService {
    UserDto getUserWithAuthorities(String email);
    UserDto getMyUserWithAuthorities();
    Iterable<UserDto> getUsersByAll()
}
