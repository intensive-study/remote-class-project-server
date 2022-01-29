package org.server.remoteclass.controller;

import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.dto.LoginDto;
import org.server.remoteclass.dto.TokenDto;
import org.server.remoteclass.entity.UserEntity;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.jwt.JwtFilter;
import org.server.remoteclass.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserRepository userRepository){
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRepository = userRepository;
    }

    // 여기에 login이 있는 이유 -> 추후에 관리자 계정을 발급할 수 있어서, usercontroller보다는 여기에 두는 게 맞다고 생각했어요.
    // signup의 경우는 관리자에겐 해당하지 않는 것 같아 usercontroller에 있습니다.
    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);
        HttpHeaders headers = new HttpHeaders();
        Optional<UserEntity> user = userRepository.findOneWithAuthoritiesByEmail(loginDto.getEmail());
        log.info("user : " + user);
        headers.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new TokenDto(jwt, user.get().getUserId()), headers, HttpStatus.OK);

    }
}
