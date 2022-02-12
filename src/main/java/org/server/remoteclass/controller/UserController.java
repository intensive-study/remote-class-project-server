package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.ResponseUserByAdminDto;
import org.server.remoteclass.dto.ResponseUserDto;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "테스트")
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome test";
    }

    // 본인 정보 조회 기능(비밀번호까지 나와서 다른 유저 조회 시 기능 구현 시엔 주의해야 합니다)
    @ApiOperation(value = "나의 정보 확인", notes = "자신의 정보를 상세하게 조회할 수 있다.")
    @GetMapping("/myself")
    public ResponseEntity<ResponseUserByAdminDto> getMyInfo(){
        return ResponseEntity.ok(userService.getMyInfoWithAuthorities());
    }

    // 사용자가 사용자 조회
    @ApiOperation(value = "사용자가 다른 사용자 조회", notes = "다른 사용자의 기본 정보(이름, 역할) 등을 조회할 수 있다.")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserDto> getUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    // 전체 유저 조회
    @ApiOperation(value = "사용자가 전체 사용자 조회", notes = "전체 사용자를 조회하며 기본 정보를 조회할 수 있다.")
    @GetMapping
    public ResponseEntity<List<ResponseUserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getUsersByAll());
    }


}
