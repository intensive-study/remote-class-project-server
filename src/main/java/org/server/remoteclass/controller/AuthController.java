package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.dto.auth.LoginDto;
import org.server.remoteclass.dto.auth.ResponseTokenDto;
import org.server.remoteclass.dto.auth.RequestTokenDto;
import org.server.remoteclass.dto.user.RequestUserDto;
import org.server.remoteclass.dto.user.ResponseUserDto;
import org.server.remoteclass.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @ApiOperation(value = "사용자 회원가입", notes = "email, name, password를 통해 회원가입한다.")
    @PostMapping("/signup")
    public ResponseEntity<ResponseUserDto> signup(@Valid @RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(requestUserDto));
    }

    @ApiOperation(value = "로그인", notes = "email, password를 통해 회원가입한다.")
    @PostMapping("/login")
    public ResponseEntity<ResponseTokenDto> authorize(@Valid @RequestBody LoginDto loginDto){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginDto));
    }

    @ApiOperation(value = "토큰 재발급", notes = "access 토큰과 refresh 토큰으로 토큰을 재발급받는다.")
    @PostMapping("/reissue")
    public ResponseEntity<ResponseTokenDto> reissue(
            @RequestBody RequestTokenDto requestTokenDto){
        return ResponseEntity.status(HttpStatus.OK).body(authService.reissue(requestTokenDto));
    }

}
