package org.server.remoteclass.jwt;

import org.server.remoteclass.exception.ExceptionHandlerFilter;
import org.server.remoteclass.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;
    private AuthService authService;
    public JwtSecurityConfig(TokenProvider tokenProvider, AuthService authService){
        this.tokenProvider = tokenProvider;
        this.authService = authService;
    }

    @Autowired
    private ExceptionHandlerFilter exceptionHandlerFilter;

    @Override
    public void configure(HttpSecurity http){
        JwtFilter customFilter = new JwtFilter(tokenProvider, authService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
//        JwtFilter 예외처리 부분에서 작성한 코드이며, 현재 주석 처리하면 에러 뜹니다.
//        http.addFilterBefore(exceptionHandlerFilter, JwtFilter.class);
    }

}
