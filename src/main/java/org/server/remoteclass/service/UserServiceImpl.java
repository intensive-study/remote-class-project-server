package org.server.remoteclass.service;

import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.UserEntity;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

}
