package org.server.remoteclass.service.user;
import org.server.remoteclass.dto.user.ResponseUserByAdminDto;
import org.server.remoteclass.dto.user.ResponseUserDto;

import java.util.List;

public interface UserService {
    ResponseUserDto getUserWithAuthorities(String email);
    ResponseUserDto getUserByUserId(Long userId);
    ResponseUserByAdminDto getMyInfoWithAuthorities();
    List<ResponseUserDto> getUsersByAll();
    // 학생 -> 강의자
    void fromStudentToLecturer();
    // 강의자 -> 학생
    void fromLecturerToStudent();
}
