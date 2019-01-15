package com.hgq.security.config;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/**
 * @author houguangqiang
 * @date 2018-12-07
 * @since 1.0
 */
public class ApplicationStartedListener implements ApplicationListener<ApplicationPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        String[] names = applicationContext.getBeanDefinitionNames();
        Arrays.stream(names).forEach(name -> {
            Class<?> type = applicationContext.getType(name);
            System.out.println(type);
        });
    }
}
