package org.server.remoteclass.service;

import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserWithAuthorities(String email){
        return UserDto.from(userRepository.findByEmail(email).orElse(null));
    }

    //현재 스프링 시큐리티 컨텍스트에 있는 유저 반환
    @Transactional(readOnly = true)
    @Override
    public UserDto getMyUserWithAuthorities(){
        return UserDto.from(SecurityUtil.getCurrentUserEmail().flatMap(userRepository::findByEmail).orElse(null));
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<UserDto> getUsersByAll(){
        Iterable<UserDto> userDtoIterable = userRepository.findAll().stream().map(user -> model)
        return userRepository.findAll();
    }

}
