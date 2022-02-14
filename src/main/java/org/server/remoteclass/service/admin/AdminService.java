package org.server.remoteclass.service.admin;

import org.server.remoteclass.dto.user.ResponseUserByAdminDto;

import java.util.List;

public interface AdminService {

    public ResponseUserByAdminDto getUser(Long userId);
    public List<ResponseUserByAdminDto> getAllUsers();
}
