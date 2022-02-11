package org.server.remoteclass.service;
import org.server.remoteclass.dto.ResponseUserByAdminDto;
import org.server.remoteclass.dto.ResponseUserDto;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResponseUserDto getUserWithAuthorities(String email);
    ResponseUserDto getUserByUserId(Long userId);
    ResponseUserByAdminDto getMyUserWithAuthorities();
    List<ResponseUserDto> getUsersByAll();
}
