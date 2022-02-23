package org.server.remoteclass.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.constant.Authority;
import org.server.remoteclass.dto.auth.LoginDto;
import org.server.remoteclass.dto.auth.ResponseTokenDto;
import org.server.remoteclass.dto.auth.RequestTokenDto;
import org.server.remoteclass.dto.user.RequestUserDto;
import org.server.remoteclass.dto.user.ResponseUserDto;
import org.server.remoteclass.entity.RefreshToken;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.exception.*;
import org.server.remoteclass.jpa.RefreshTokenRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           AuthenticationManagerBuilder authenticationManagerBuilder,
                           TokenProvider tokenProvider,
                           RefreshTokenRepository refreshTokenRepository){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public ResponseUserDto signup(RequestUserDto requestUserDto){
        if(userRepository.existsByEmail(requestUserDto.getEmail())){
            throw new EmailDuplicateException("이미 가입되어 있는 유저입니다", ErrorCode.EMAIL_DUPLICATION);
        }

        User user = User.builder()
                .email(requestUserDto.getEmail())
                .name(requestUserDto.getName())
                .password(bCryptPasswordEncoder.encode(requestUserDto.getPassword()))
                .userRole(UserRole.ROLE_STUDENT)
                .authority(Authority.ROLE_USER)
                .build();

        return ResponseUserDto.from(userRepository.save(user));
    }

    @Override
    public ResponseTokenDto login(LoginDto loginDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        ResponseTokenDto responseTokenDto = tokenProvider.createToken(authentication);
        log.info("authentication getname" + authentication.getName());
        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName()) // getName이 현재 로그인 하려고 하는 사람의 email입니다. 커스텀하지 않았다면 기본적으로는 username일 것 같습니다.
                .value(responseTokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        // 5. 토큰 발급
        return responseTokenDto;
    }

    @Override
    public ResponseTokenDto reissue(RequestTokenDto requestTokenDto){
        // 1. Refresh Token 검증
        if(!tokenProvider.validateToken(requestTokenDto.getRefreshToken())){
            throw new UnauthorizedException("Refresh Token이 유효하지 않습니다.", ErrorCode.UNAUTHORIZED);
        }
        // 2. Access Token 에서 User Id 가져오기
        Authentication authentication = tokenProvider.getAuthentication(requestTokenDto.getAccessToken());
        // 3. 저장소에서 User Id를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new ForbiddenException("로그아웃 된 사용자입니다.", ErrorCode.FORBIDDEN));
        // 4. Refresh Token 일치하는 지 검사
        if(!refreshToken.getValue().equals(requestTokenDto.getRefreshToken())){
            throw new BadRequestArgumentException("토큰의 유저 정보가 일치하지 않습니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
        }
        // 5. 새로운 토큰 생성
        ResponseTokenDto responseTokenDto = tokenProvider.createToken(authentication);
        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(responseTokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        return responseTokenDto;
    }

}
