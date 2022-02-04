package org.server.remoteclass.controller;

import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.service.UserService;
import org.server.remoteclass.vo.RequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome test";
    }

}
