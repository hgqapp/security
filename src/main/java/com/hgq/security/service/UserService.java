package com.hgq.security.service;

import com.hgq.security.beans.dto.UsersDto;
import com.hgq.security.beans.vo.UsersVo;
import com.hgq.security.model.Users;
import com.hgq.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    public Long crate(UsersDto user) {
        Users userModel = modelMapper.map(user, Users.class);
        userModel.setSalt("1");
        return userRepository.save(userModel).getUserId();
    }

    public UsersVo getByUserId(Long userId) {
        Optional<Users> users = userRepository.findById(userId);
        return users.map(u -> modelMapper.map(u, UsersVo.class)).orElse(null);
    }
}
