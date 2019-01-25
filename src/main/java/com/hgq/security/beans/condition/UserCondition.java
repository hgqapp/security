package com.hgq.security.beans.condition;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author houguangqiang
 * @date 2019-01-25
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class UserCondition {
    private String username;
    private String email;
    private String phone;
    private Boolean enabled;
}
