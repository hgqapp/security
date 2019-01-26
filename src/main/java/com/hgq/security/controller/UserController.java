package com.hgq.security.controller;

import com.hgq.security.beans.condition.UserCondition;
import com.hgq.security.beans.dto.UsersDto;
import com.hgq.security.beans.vo.UsersPageVo;
import com.hgq.security.beans.vo.UsersVo;
import com.hgq.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Secured("USER-CREATE")
    @PostMapping("/create")
    public Long create(UsersDto user) {
        return userService.crate(user);
    }

    @Secured("USER-UPDATE")
    @PostMapping("/update")
    public Long update(UsersDto user) {
        return userService.update(user);
    }

    @Secured("USER-GET")
    @GetMapping("/get")
    public UsersVo get(Long userId) {
        return userService.getByUserId(userId);
    }

    @Secured("USER-DELETE")
    @PostMapping("/delete")
    public Long delete(Long userId) {
        return userService.delete(userId);
    }

    @Secured("USER-PAGE")
    @GetMapping("/page")
    public Page<UsersPageVo> page(UserCondition condition, Pageable pageable) {
        return userService.page(condition, pageable);
    }
}
