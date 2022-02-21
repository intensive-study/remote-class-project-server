package org.server.remoteclass.util;

import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AccessVerification {

    @Autowired
    UserRepository userRepository;

    public  boolean hasAccessOnlyStudent() {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자입니다.", ErrorCode.ID_NOT_EXIST));
        if(user.getUserRole() != UserRole.ROLE_STUDENT) throw new ForbiddenException("접근 권한이 없습니다", ErrorCode.FORBIDDEN);
        return true;
    }

    public boolean hasAccessOnlyLecturer() {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자입니다.", ErrorCode.ID_NOT_EXIST));

        if(user.getUserRole() != UserRole.ROLE_LECTURER) throw new ForbiddenException("접근 권한이 없습니다", ErrorCode.FORBIDDEN);
        return true;
    }
}
