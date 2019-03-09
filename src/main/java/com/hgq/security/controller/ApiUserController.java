package com.hgq.security.controller;

import com.hgq.security.beans.dto.UsersDto;
import com.hgq.security.beans.vo.UsersVo;
import com.hgq.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Long create(UsersDto user) {
        return userService.create(user);
    }


    @GetMapping("/get")
    public UsersVo get(Long userId) {
        return userService.getByUserId(userId);
    }
}
