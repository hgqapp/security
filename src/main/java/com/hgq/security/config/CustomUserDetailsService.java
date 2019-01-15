package com.hgq.security.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;
import java.util.List;

public class CustomUserDetailsService extends JdbcDaoImpl {

    private static final String USERS_BY_USERNAME_QUERY = "select user_id,username,password,phone,email,enabled "
            + "from users " + "where username = ?";

    private static final String AUTHORITIES_BY_USERNAME_QUERY = "select u.username,a.authority from users u,user_authority_relations ua, " +
            "authorities a where u.username=? and u.user_id=ua.user_id and ua.authority_id=a.authority_id";

    private static final String GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select distinct u.username, a.authority_id,a.authority from users u," +
            "user_group_relations ug,group_authority_relations ga, authorities a where u.username=? " +
            "and u.user_id=ug.user_id and ug.group_id=ga.group_id and ga.authority_id=a.authority_id";

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
        return getJdbcTemplate().query(getUsersByUsernameQuery(),
                new String[] { username }, (rs, rowNum) -> {
                    Long userId = rs.getLong(1);
                    String username1 = rs.getString(2);
                    String password = rs.getString(2);
                    String phone = rs.getString(3);
                    String email = rs.getString(4);
                    boolean enabled = rs.getBoolean(5);
                    return new CurrentUser(userId, username1, password, phone, email, enabled, true, true, true,
                            AuthorityUtils.NO_AUTHORITIES);
                });
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
