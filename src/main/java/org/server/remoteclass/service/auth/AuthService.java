package org.server.remoteclass.service.auth;

import org.server.remoteclass.dto.auth.LoginDto;
import org.server.remoteclass.dto.auth.TokenDto;
import org.server.remoteclass.dto.auth.TokenRequestDto;
import org.server.remoteclass.dto.user.RequestUserDto;
import org.server.remoteclass.dto.user.ResponseUserDto;

public interface AuthService {
    ResponseUserDto signup(RequestUserDto requestUserDto);
    TokenDto login(LoginDto loginDto);
    TokenDto reissue(TokenRequestDto tokenRequestDto);
}
