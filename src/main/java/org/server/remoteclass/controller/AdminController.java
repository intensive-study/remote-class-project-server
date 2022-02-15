package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.user.ResponseUserByAdminDto;
import org.server.remoteclass.service.AdminService;
import org.server.remoteclass.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @ApiOperation(value = "테스트용")
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome test";
    }

    // 관리자가 사용자 조회
    @ApiOperation(value = "관리자가 사용자 조회", notes = "사용자의 모든 정보를 조회할 수 있다.")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUserByAdminDto> getUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(adminService.getUser(userId));
    }

    // 관리자가 전체 유저 조회
    @ApiOperation(value = "관리자가 모든 사용자 조회", notes = "모든 사용자의 상세한 정보를 알 수 있다.")
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUserByAdminDto>> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }
}
