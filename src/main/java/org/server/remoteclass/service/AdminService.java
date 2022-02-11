package org.server.remoteclass.service;

import org.server.remoteclass.dto.ResponseUserByAdminDto;

import java.util.List;

public interface AdminService {

    public ResponseUserByAdminDto getUser(Long userId);
    public List<ResponseUserByAdminDto> getAllUsers();
}