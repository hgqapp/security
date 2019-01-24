package com.hgq.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author houguangqiang
 * @date 2018-12-07
 * @since 1.0
 */
@Component
public class ApplicationStartedListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartedListener.class);

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        logger.info("应用启动完成");
    }
}
