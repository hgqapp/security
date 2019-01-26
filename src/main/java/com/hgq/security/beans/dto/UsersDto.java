package com.hgq.security.beans.dto;

import com.hgq.security.support.validation.ValidationGroup.Create;
import com.hgq.security.support.validation.ValidationGroup.Update;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {

    @NotNull(groups = Update.class)
    @Null(groups = Create.class)
    private Long userId;
    @NotNull(groups = Create.class)
    @Size(min = 5, max = 30)
    private String username;
    @NotNull(groups = Create.class)
    @Size(min = 6, max = 30)
    private String password;
    @NotNull(groups = Create.class)
    @Email
    @Size(min = 3, max = 50)
    private String email;
    @NotBlank(groups = Create.class)
    @Pattern(regexp = "^(\\+?0?86-?)?1[345789]\\d{9}$", message = "^手机号码错误")
    private String phone;
    private Boolean enabled;

}
