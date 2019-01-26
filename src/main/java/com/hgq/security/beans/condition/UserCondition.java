package com.hgq.security.beans.condition;

import com.hgq.security.model.QUsers;
import com.querydsl.core.BooleanBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.hgq.security.beans.condition.Criterias.*;


/**
 * @author houguangqiang
 * @date 2019-01-25
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class UserCondition implements Criterias {
    private String username;
    private String email;
    private String phone;
    private Boolean enabled;
    private Long createTimeStart;
    private Long createTimeEnd;

    @Override
    public BooleanBuilder create() {
        QUsers users = QUsers.users;
        BooleanBuilder builder = new BooleanBuilder();
        andBetween(builder, users.createTime, createTimeStart, createTimeEnd);
        andCntains(builder, users.username, username);
        andCntains(builder, users.email, email);
        andCntains(builder, users.phone, phone);
        andEq(builder, users.enabled, enabled);
        return builder;
    }
}
