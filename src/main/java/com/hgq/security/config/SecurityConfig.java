package com.hgq.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/api/**")
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .httpBasic();
        }

    }

    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                    .antMatchers("/css/**", "/index").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login").failureUrl("/login-error").permitAll()
                    .and()
                .logout()
                    .logoutUrl("/my/logout")
                    .logoutSuccessUrl("/my/index")
                    .invalidateHttpSession(true)
                    .deleteCookies("cookies");
        }
    }

    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
//        @Override
//        protected MethodSecurityExpressionHandler createExpressionHandler() {
//            // ... create and return custom MethodSecurityExpressionHandler ...
//            return null;
//        }

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JdbcUserDetailsManager userDetailsService = auth.jdbcAuthentication().getUserDetailsService();
        userDetailsService.setDataSource(dataSource);
    }
}
