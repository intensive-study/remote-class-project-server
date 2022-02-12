package org.server.remoteclass.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.user.ResponseUserByAdminDto;
import org.server.remoteclass.dto.user.ResponseUserDto;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BeanConfiguration beanConfiguration){
        this.userRepository = userRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @Override
    public ResponseUserDto getUserWithAuthorities(String email){
        return ResponseUserDto.from(userRepository.findByEmail(email).orElse(null));
    }

    //본인의 정보 반환
    @Override
    public ResponseUserByAdminDto getMyInfoWithAuthorities(){
        return ResponseUserByAdminDto.from(SecurityUtil.getCurrentUserEmail().flatMap(userRepository::findByEmail).orElse(null));
    }

    @Override
    public ResponseUserDto getUserByUserId(Long userId){
        return ResponseUserDto.from(userRepository.findByUserId(userId).orElse(null));
    }

    //전체 유저 정보 조회
    @Override
    public List<ResponseUserDto> getUsersByAll(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, ResponseUserDto.class)).collect(Collectors.toList());
    }

}
