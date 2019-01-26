package com.hgq.security.support.security;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.Objects;

/**
 * @author houguangqiang
 * @date 2018-12-05
 * @since 1.0
 */
public class ContextHolder {

    private static final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    private static final String ROLE_PREFIX = "ROLE_";

    private ContextHolder() {}

    public static boolean isUserInRole(String role) {
        return isGranted(role);
    }

    public static String getRemoteUser() {
        Authentication auth = getAuthentication();

        if ((auth == null) || (auth.getPrincipal() == null)) {
            return null;
        }

        if (auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }

        return auth.getPrincipal().toString();
    }

    public static Principal getUserPrincipal() {
        Authentication auth = getAuthentication();
        if ((auth == null) || (auth.getPrincipal() == null)) {
            return null;
        }
        return auth;
    }

    private static Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!trustResolver.isAnonymous(auth)) {
            return auth;
        }

        return null;
    }

    private static boolean isGranted(String role) {
        Objects.requireNonNull(role, "parameter role not be null");
        Authentication auth = getAuthentication();

        if (!role.startsWith(ROLE_PREFIX)) {
            role = ROLE_PREFIX + role;
        }

        if ((auth == null) || (auth.getPrincipal() == null)) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (role.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }

        return false;
    }
}
