package com.hgq.security.beans.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class UsersPageVo {

    private Long userId;
    private String username;
    private String email;
    private String phone;
    private Boolean enabled;
    private Long createTime;
    private Long updateTime;

}
