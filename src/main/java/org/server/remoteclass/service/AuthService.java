package org.server.remoteclass.service;

import org.server.remoteclass.dto.LoginDto;
import org.server.remoteclass.dto.TokenDto;
import org.server.remoteclass.dto.TokenRequestDto;
import org.server.remoteclass.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService {
    UserDto signup(UserDto userDto);
    TokenDto login(LoginDto loginDto);
    TokenDto reissue(TokenRequestDto tokenRequestDto);
}
