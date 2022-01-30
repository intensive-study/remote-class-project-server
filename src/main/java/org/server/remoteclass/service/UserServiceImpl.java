package org.server.remoteclass.service;

import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.dto.LoginDto;
import org.server.remoteclass.dto.TokenDto;
import org.server.remoteclass.dto.TokenRequestDto;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.Authority;
import org.server.remoteclass.entity.RefreshToken;
import org.server.remoteclass.entity.UserEntity;
import org.server.remoteclass.jpa.RefreshTokenRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.jwt.TokenProvider;
import org.server.remoteclass.util.SecurityUtil;
import org.server.remoteclass.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           Environment env,
                           AuthenticationManagerBuilder authenticationManagerBuilder,
                           TokenProvider tokenProvider,
                           RefreshTokenRepository refreshTokenRepository){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    @Override
    public UserDto signup(UserDto userDto){
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        UserEntity user = UserEntity.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .userRole(UserRole.ROLE_STUDENT)
                .authority(Authority.ROLE_USER)
                .build();

        return UserDto.from(userRepository.save(user));
    }

    @Transactional
    @Override
    public TokenDto login(LoginDto loginDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.createToken(authentication);
        log.info("authentication getname" + authentication.getName());
        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName()) // getName이 현재 로그인 하려고 하는 사람의 email입니다. 커스텀하지 않았다면 기본적으로는 username일 것 같습니다.
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    @Override
    public TokenDto reissue(TokenRequestDto tokenRequestDto){
        // 1. Refresh Token 검증
        if(!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw  new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 User Id 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 User Id를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는 지 검사
        if(!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.createToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserWithAuthorities(String email){
        return UserDto.from(userRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<UserEntity> getMyUserWithAuthorities(){
        return SecurityUtil.getCurrentUserEmail().flatMap(userRepository::findOneWithAuthoritiesByEmail);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<UserEntity> getUsersByAll(){
        return userRepository.findAll();
    }

//    스프링 시큐리티에서 로그인 시에 사용되는 함수입니다. 기본적으로 username, password로 구현되어 있는 함수를 이메일, 비밀번호로 변경하였습니다.
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByEmail(email).get());
//        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findOneWithAuthoritiesByEmail(email).get());
        if(userEntity.isPresent()){
            User user = new User(userEntity.get().getEmail(), userEntity.get().getPassword(), true, true, true, true, new ArrayList<>());
            return user;
        }
        else{
            throw new UsernameNotFoundException(email + " not found!");
        }
    }
}
