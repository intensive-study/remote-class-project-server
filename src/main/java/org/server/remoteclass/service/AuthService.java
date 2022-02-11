package org.server.remoteclass.service;

import org.server.remoteclass.dto.*;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService {
    ResponseUserDto signup(RequestUserDto requestUserDto);
    TokenDto login(LoginDto loginDto);
    TokenDto reissue(TokenRequestDto tokenRequestDto);
}
