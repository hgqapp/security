package com.hgq.security.service;

import com.hgq.security.beans.condition.Criterias;
import com.hgq.security.beans.dto.UsersDto;
import com.hgq.security.beans.vo.UsersPageVo;
import com.hgq.security.beans.vo.UsersVo;
import com.hgq.security.config.ValidationGroup;
import com.hgq.security.model.QUsers;
import com.hgq.security.model.Users;
import com.hgq.security.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Optional;

import static org.springframework.util.Assert.isTrue;

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

    @Validated({ValidationGroup.Create.class, Default.class})
    public Long crate(@Valid UsersDto user) {
        checkInput(user);
        Users userModel = modelMapper.map(user, Users.class);
        userModel.setPassword(passwordEncoder.encode(user.getPassword()));
        userModel.setCreateTime(System.currentTimeMillis());
        userModel.setUpdateTime(userModel.getCreateTime());
        return userRepository.save(userModel).getUserId();
    }

    @Validated({ValidationGroup.Update.class, Default.class})
    public Long update(@Valid UsersDto user) {
        checkInput(user);
        Users userModel = modelMapper.map(user, Users.class);
        userModel.setUpdateTime(System.currentTimeMillis());
        userRepository.save(userModel);
        return user.getUserId();
    }

    private void checkInput(UsersDto user) {
        Long userId = user.getUserId();
        if (userId != null) {
            Optional<Users> dbUser = userRepository.findById(userId);
            isTrue(dbUser.isPresent(), () -> String.format("用户id\"%s\"不存在", userId));
        }
        String username = user.getUsername();
        if (username != null) {
            Optional<Users> dbUser = userRepository.findOne(QUsers.users.username.eq(username));
            isTrue(!dbUser.isPresent() || dbUser.get().getUserId().equals(user.getUserId()), () -> String.format("用户名\"%s\"已经存在", username));
        }
        String phone = user.getPhone();
        if (phone != null) {
            Optional<Users> dbUser  = userRepository.findOne(QUsers.users.phone.eq(phone));
            isTrue(!dbUser.isPresent() || dbUser.get().getUserId().equals(user.getUserId()), () -> String.format("手机号\"%s\"已经存在", phone));
        }
        String email = user.getEmail();
        if (email != null) {
            Optional<Users> dbUser  = userRepository.findOne(QUsers.users.email.eq(email));
            isTrue(!dbUser.isPresent() || dbUser.get().getUserId().equals(user.getUserId()), () -> String.format("邮箱\"%s\"已经存在", email));
        }
    }

    public UsersVo getByUserId(@NotNull Long userId) {
        Optional<Users> users = userRepository.findById(userId);
        return users.map(u -> modelMapper.map(u, UsersVo.class)).orElse(null);
    }

    public Page<UsersPageVo> page(Criterias condition, Pageable pageable) {
        Page<Users> page = userRepository.findAll(condition.create(), pageable);
        return page.map(UsersPageVo::new);
    }

    public Long delete(@NotNull Long userId) {
        userRepository.deleteById(userId);
        return userId;
    }
}
