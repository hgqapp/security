package com.hgq.security.beans.dto;

import com.hgq.security.config.ValidationGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class UsersDto {

    @NotNull(groups = ValidationGroup.Update.class)
    private Long userId;
    @NotNull(groups = ValidationGroup.Create.class)
    @Size(min = 6, max = 30)
    private String username;
    @NotNull(groups = ValidationGroup.Create.class)
    @Size(min = 6, max = 30)
    private String password;
    @NotNull(groups = ValidationGroup.Create.class)
    @Email
    @Size(min = 3, max = 50)
    private String email;
    @NotBlank(groups = ValidationGroup.Create.class)
    @Pattern(regexp = "^(\\+?0?86-?)?1[345789]\\d{9}$", message = "手机号码错误")
    private String phone;
    private Boolean enabled;

}
