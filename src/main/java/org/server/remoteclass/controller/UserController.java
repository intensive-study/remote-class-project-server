package org.server.remoteclass.controller;

import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.service.UserService;
import org.server.remoteclass.vo.RequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome test";
    }

    // 본인 정보 조회 기능(비밀번호까지 나와서 다른 유저 조회 시 기능 구현 시엔 주의해야 합니다)
    @GetMapping("/myself")
    public ResponseEntity<UserDto> getUser(){
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getUsersByAll());
//        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }


}
