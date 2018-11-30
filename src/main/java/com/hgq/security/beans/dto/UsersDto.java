package com.hgq.security.beans.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class UsersDto {

    private Long userId;
    private String username;
    private String password;
    private Boolean enabled;

}
