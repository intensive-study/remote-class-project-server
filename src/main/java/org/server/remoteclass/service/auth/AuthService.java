package org.server.remoteclass.service.auth;

import org.server.remoteclass.dto.auth.LoginDto;
import org.server.remoteclass.dto.auth.ResponseTokenDto;
import org.server.remoteclass.dto.auth.RequestTokenDto;
import org.server.remoteclass.dto.user.RequestUserDto;
import org.server.remoteclass.dto.user.ResponseUserDto;

public interface AuthService {
    ResponseUserDto signup(RequestUserDto requestUserDto);
    ResponseTokenDto login(LoginDto loginDto);
    ResponseTokenDto reissue(RequestTokenDto requestTokenDto);
    // 재발급한 토큰을 DB에 업데이트한다.
    void updateToken(RequestTokenDto requestTokenDto);
}
