package com.hgq.security.controller;

import com.hgq.security.beans.dto.UsersDto;
import com.hgq.security.beans.vo.UsersVo;
import com.hgq.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@RestController
@RequestMapping("/index")
public class IndexController {

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
