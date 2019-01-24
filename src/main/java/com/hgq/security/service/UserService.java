package com.hgq.security.service;

import com.hgq.security.beans.dto.UsersDto;
import com.hgq.security.beans.vo.UsersVo;
import com.hgq.security.config.ValidationGroup.Create;
import com.hgq.security.config.ValidationGroup.Update;
import com.hgq.security.model.Users;
import com.hgq.security.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Optional;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Service
@Validated
public class UserService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Long crate(@Validated({Create.class, Default.class}) UsersDto user) {
        Users userModel = modelMapper.map(user, Users.class);
        userModel.setPassword(passwordEncoder.encode(user.getPassword()));
        userModel.setCreateTime(System.currentTimeMillis());
        userModel.setUpdateTime(userModel.getCreateTime());
        return userRepository.save(userModel).getUserId();
    }

    public UsersVo getByUserId(@NotNull Long userId) {
        Optional<Users> users = userRepository.findById(userId);
        return users.map(u -> modelMapper.map(u, UsersVo.class)).orElse(null);
    }

    public boolean update(@Validated({Update.class, Default.class}) UsersDto user) {
        Users userModel = modelMapper.map(user, Users.class);
        userModel.setUpdateTime(System.currentTimeMillis());
        return false;
    }
}
