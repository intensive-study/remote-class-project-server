package org.server.remoteclass.controller;

import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.dto.*;
import org.server.remoteclass.service.AuthService;
import org.server.remoteclass.service.UserService;
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

    @PostMapping("/signup")
    public ResponseEntity<ResponseUserDto> signup(@Valid @RequestBody RequestUserDto requestUserDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(requestUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

}
