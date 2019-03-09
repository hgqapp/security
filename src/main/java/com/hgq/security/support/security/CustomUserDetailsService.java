package com.hgq.security.support.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;
import java.util.List;

public class CustomUserDetailsService extends JdbcDaoImpl {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private static final String USERS_BY_USERNAME_QUERY = "select user_id,username,password,phone,email,enabled "
            + "from users " + "where username = ?";

    private static final String AUTHORITIES_BY_USERNAME_QUERY = "select u.username,a.authority from users u,user_authority_relations ua, " +
            "authorities a where u.username=? and u.user_id=ua.user_id and ua.authority_id=a.authority_id";

    private static final String GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select distinct u.username, a.authority_id,a.authority from users u," +
            "user_role_relations ur,role_authority_relations ra, authorities a where u.username=? " +
            "and u.user_id=ur.user_id and ur.role_id=ra.role_id and ra.authority_id=a.authority_id";

    public CustomUserDetailsService(DataSource dataSource) {
        setUsersByUsernameQuery(USERS_BY_USERNAME_QUERY);
        setAuthoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);
        setGroupAuthoritiesByUsernameQuery(GROUP_AUTHORITIES_BY_USERNAME_QUERY);
        setEnableGroups(true);
        setDataSource(dataSource);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected List<UserDetails> loadUsersByUsername(String username) {
        log.trace("==>> 加载用户【{}】的基本信息", username);
        return getJdbcTemplate().query(getUsersByUsernameQuery(),
                new String[] { username }, (rs, rowNum) -> {
                    Long userId = rs.getLong(1);
                    String username1 = rs.getString(2);
                    String password = rs.getString(3);
                    String phone = rs.getString(4);
                    String email = rs.getString(5);
                    boolean enabled = rs.getBoolean(6);
                    return new CurrentUser(userId, username1, password, phone, email, enabled, true, true, true,
                            AuthorityUtils.NO_AUTHORITIES);
                });
    }

    @Override
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        log.trace("==>> 加载用户【{}】的权限列表", username);
        return super.loadUserAuthorities(username);
    }

    @Override
    protected List<GrantedAuthority> loadGroupAuthorities(String username) {
        log.trace("==>> 加载用户【{}】的用户组权限列表", username);
        return super.loadGroupAuthorities(username);
    }

    @Override
    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromUserQuery.getUsername();

        if (!isUsernameBasedPrimaryKey()) {
            returnUsername = username;
        }
        CurrentUser currentUser = userFromUserQuery instanceof CurrentUser ? ((CurrentUser) userFromUserQuery) : null;
        if (currentUser != null) {
            return new CurrentUser(currentUser.getUserId(), returnUsername, currentUser.getPassword(),
                    currentUser.getPhone(), currentUser.getEmail(), currentUser.isEnabled(), currentUser.isAccountNonExpired(),
                    currentUser.isCredentialsNonExpired(), currentUser.isAccountNonLocked(),
                    combinedAuthorities);
        }
        return new User(returnUsername, userFromUserQuery.getPassword(),
                userFromUserQuery.isEnabled(), userFromUserQuery.isAccountNonExpired(), userFromUserQuery.isCredentialsNonExpired(),
                userFromUserQuery.isAccountNonLocked(), combinedAuthorities);
    }
}
