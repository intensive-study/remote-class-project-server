package org.server.remoteclass.service;

import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.Authority;
import org.server.remoteclass.entity.UserEntity;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    Environment env;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    @Transactional
    @Override
    public UserDto signup(UserDto userDto){
        if(userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null){
//         // 예외처리
            return null;
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        UserEntity user = UserEntity.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .authorities(Collections.singleton(authority))
                .build();

        return UserDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String email){
        return UserDto.from(userRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> getMyUserWithAuthorities(){
        return SecurityUtil.getCurrentUserEmail().flatMap(userRepository::findOneWithAuthoritiesByEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<UserEntity> getUsersByAll(){
        return userRepository.findAll();
    }

//    스프링 시큐리티에서 로그인 시에 사용되는 함수입니다. 기본적으로 username, password로 구현되어 있는 함수를 이메일, 비밀번호로 변경하였습니다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findOneWithAuthoritiesByEmail(email).get());
        if(userEntity.isPresent()){
            User user = new User(userEntity.get().getEmail(), userEntity.get().getPassword(), true, true, true, true, new ArrayList<>());
            return user;
        }
        else{
            throw new UsernameNotFoundException(email + " not found!");
        }
    }
}
