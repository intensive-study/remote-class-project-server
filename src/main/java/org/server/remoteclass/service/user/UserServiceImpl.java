package org.server.remoteclass.service.user;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.dto.user.ResponseUserByAdminDto;
import org.server.remoteclass.dto.user.ResponseUserDto;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.AuthDuplicateException;
import org.server.remoteclass.exception.EmailDuplicateException;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.exception.IdNotExistException;
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
        return ResponseUserByAdminDto.from(SecurityUtil.getCurrentUserEmail().flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST)));
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

    // 에러가 발생하는 지점입니다.
    @Transactional
    @Override
    public void fromStudentToLecturer(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        if(user.getUserRole() == UserRole.ROLE_LECTURER){
            throw new AuthDuplicateException("현재 해당 권한을 가지고 있습니다", ErrorCode.AUTH_DUPLICATION);
        }
        else{
            user.setUserRole(UserRole.ROLE_LECTURER);
            // 코드상으로 두 줄이 잘 작동하는 데, 저장하는 과정에서 오류가 생기는 것 같습니다.
        }
    }

    @Transactional
    @Override
    public void fromLecturerToStudent(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            throw new AuthDuplicateException("현재 해당 권한을 가지고 있습니다", ErrorCode.AUTH_DUPLICATION);
        }
        else{
            user.setUserRole(UserRole.ROLE_STUDENT);
        }
    }
}
