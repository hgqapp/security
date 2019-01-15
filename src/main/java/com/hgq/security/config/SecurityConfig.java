package com.hgq.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.sql.DataSource;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new CustomUserDetailsService(dataSource);
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private UserDetailsService userDetailsService;

        public ApiWebSecurityConfigurationAdapter(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.userDetailsService(userDetailsService);
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
        private UserDetailsService userDetailsService;
        public FormLoginWebSecurityConfigurerAdapter(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.userDetailsService(userDetailsService);
            http
                .authorizeRequests()
                    .antMatchers("/css/**", "/index").permitAll()
                    .anyRequest().authenticated().and()
                .formLogin();
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

//        @Autowired
//        public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
//            auth.jdbcAuthentication()
//                    .dataSource(dataSource)
//                    .authoritiesByUsernameQuery("")
//                    .groupAuthoritiesByUsername("")
//                    .usersByUsernameQuery("")
//                    .userCache(null);
//        }

}
