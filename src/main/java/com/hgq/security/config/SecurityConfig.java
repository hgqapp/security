package com.hgq.security.config;

import com.hgq.security.support.security.CustomUserDetailsService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class SecurityConfig {

    private DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(dataSource);
    }

    @EnableWebSecurity
    @ConditionalOnProperty(prefix = "spring.security", name = "enabled", havingValue = "false")
    class WebSecurityDisabled {
        @Configuration
        @Order(-1)
        class DisableWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                        .csrf().disable()
                        .authorizeRequests().anyRequest().permitAll();
            }
        }
    }

    @EnableWebSecurity
    @ConditionalOnProperty(prefix = "spring.security", name = "enabled", havingValue = "true")
    class WebSecurityEnabled {

        @Configuration
        @Order(1)
        class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

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
        class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
            private UserDetailsService userDetailsService;
            public FormLoginWebSecurityConfigurerAdapter(UserDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
            }
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.userDetailsService(userDetailsService);
                http
                        .authorizeRequests()
                        .antMatchers("/css/**", "/index", "/login").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .csrf().disable()
                        .formLogin();
            }
        }

        @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
        class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

            @Override
            protected MethodSecurityExpressionHandler createExpressionHandler() {
                // ... create and return custom MethodSecurityExpressionHandler ...
                return defaultMethodSecurityExpressionHandler();
            }

            @Bean
            public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
                DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
                expressionHandler.setPermissionEvaluator(permissionEvaluator());
                expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService()));
                return expressionHandler;
            }

            @Bean
            public PermissionEvaluator permissionEvaluator() {
                AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService());
                permissionEvaluator.setPermissionFactory(permissionFactory());
                return permissionEvaluator;
            }

            @Bean
            public JdbcMutableAclService aclService() {
                JdbcMutableAclService aclService = new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
                aclService.setClassIdentityQuery("SELECT @@IDENTITY");
                aclService.setSidIdentityQuery("SELECT @@IDENTITY");
                return aclService;
            }

            @Bean
            public LookupStrategy lookupStrategy() {
                BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(dataSource, aclCache(),
                        aclAuthorizationStrategy(), permissionGrantingStrategy());
                lookupStrategy.setPermissionFactory(permissionFactory());
                return lookupStrategy;
            }

            @Bean
            public PermissionFactory permissionFactory() {
                return new DefaultPermissionFactory();
            }

            @Bean
            public AclCache aclCache() {
                return new SpringCacheBasedAclCache(new ConcurrentMapCache("acl-cache"),
                        permissionGrantingStrategy(), aclAuthorizationStrategy());
            }

            @Bean
            public PermissionGrantingStrategy permissionGrantingStrategy() {
                return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
            }

            @Bean
            public AclAuthorizationStrategy aclAuthorizationStrategy() {
                return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            @Override
            protected AccessDecisionManager accessDecisionManager() {
                List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
                ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
                expressionAdvice.setExpressionHandler(getExpressionHandler());
                decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expressionAdvice));
                decisionVoters.add(new RoleVoter());
                decisionVoters.add(new AuthenticatedVoter());
                decisionVoters.add(new AuthorityVoter());
                return new UnanimousBased(decisionVoters);
            }

            @Override
            protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
                // 这里可以自定义注解解析
                return super.customMethodSecurityMetadataSource();
            }

            class AuthorityVoter extends RoleVoter {

                public AuthorityVoter() {
                    setRolePrefix("API-");
                }
            }
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

}
