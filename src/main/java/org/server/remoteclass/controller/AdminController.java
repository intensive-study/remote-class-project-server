package org.server.remoteclass.controller;

import org.server.remoteclass.dto.ResponseUserByAdminDto;
import org.server.remoteclass.dto.ResponseUserDto;
import org.server.remoteclass.dto.StudentDto;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.service.AdminService;
import org.server.remoteclass.service.StudentService;
import org.server.remoteclass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final StudentService studentService;
    private final AdminService adminService;

    @Autowired
    public AdminController(StudentService studentService, AdminService adminService){
        this.studentService = studentService;
        this.adminService = adminService;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome test";
    }

    // 관리자가 사용자 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserByAdminDto> getUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(adminService.getUser(userId));
    }

    // 곤리자가 전체 유저 조회
    @GetMapping
    public ResponseEntity<List<ResponseUserByAdminDto>> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }
}
