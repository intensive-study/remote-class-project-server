package org.server.remoteclass.service.user;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.dto.user.ResponseUserByAdminDto;
import org.server.remoteclass.dto.user.ResponseUserDto;
import org.server.remoteclass.entity.User;
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
    @Override
    public void fromStudentToLecturer() {
        User user = SecurityUtil.getCurrentUserEmail().flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        if(user.getUserRole() == UserRole.ROLE_LECTURER){
            // 예외처리 필요
        }
        else{
            // 스프링 시큐리티 컨텍스트에서 얻어낸 유저 정보로 DB의 user 정보를 꺼내서 수정하는 과정입니다.
            User user_db = userRepository.findByEmail(user.getEmail())
                            .orElseThrow(() -> new IdNotExistException("해당하는 회원이 없습니다.", ErrorCode.ID_NOT_EXIST));
            log.info("이메일 " + user_db.getEmail());
            log.info("유저번호 : " + user.getUserId());
            log.info("이름" + user_db.getName());
            user_db.setUserRole(UserRole.ROLE_LECTURER);
            user_db.setName("바뀌어라");
            // 코드상으로 두 줄이 잘 작동하는 데, 저장하는 과정에서 오류가 생기는 것 같습니다.
        }
    }

    @Override
    public void fromLecturerToStudent() {
        User user = SecurityUtil.getCurrentUserEmail().flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            // 예외처리 필요
        }
        else{
            // 스프링 시큐리티 컨텍스트에서 얻어낸 유저 정보로 DB의 user 정보를 꺼내서 수정하는 과정입니다.
            User user_db = userRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new IdNotExistException("해당하는 회원이 없습니다.", ErrorCode.ID_NOT_EXIST));
            user_db.setUserRole(UserRole.ROLE_STUDENT);
            userRepository.save(user_db);
        }
    }
}
