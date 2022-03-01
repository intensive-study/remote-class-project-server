package org.server.remoteclass.service.user;
import org.server.remoteclass.dto.user.RequestUpdateUserDto;
import org.server.remoteclass.dto.user.ResponseUserByAdminDto;
import org.server.remoteclass.dto.user.ResponseUserDto;

import java.util.List;

public interface UserService {
    ResponseUserDto getUserWithAuthorities(String email);
    ResponseUserDto getUserByUserId(Long userId);
    ResponseUserByAdminDto getMyInfoWithAuthorities();
    List<ResponseUserDto> getUsersByAll();
    void updateUser(RequestUpdateUserDto requestUpdateUserDto);
    // 학생 -> 강의자
    void fromStudentToLecturer(Long userId);
    // 강의자 -> 학생
    void fromLecturerToStudent(Long userId);
}
