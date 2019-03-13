package com.hgq.security.config;

import com.github.jmnarloch.spring.boot.modelmapper.ModelMapperConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author houguangqiang
 * @date 2019-01-28
 * @since 1.0
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapperConfigurer modelMapper() {
        return modelMapper -> modelMapper.getConfiguration().setPropertyCondition(ctx -> ctx.getSource() != null);
    }
//    @Configuration
//    @ConditionalOnExpression("'${spring.jpa.hibernate.ddl-auto}'.equals('create') || '${spring.jpa.hibernate.ddl-auto}'.equals('create-drop')")
//    class DatabaseInitializerConfiguration implements ApplicationContextAware {
//
//        private ApplicationContext applicationContext;
//
//        @Override
//        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//            this.applicationContext = applicationContext;
//        }
//
//        @Bean
//        public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator(DataSource dataSource) {
//            applicationContext.publishEvent(new DataSourceSchemaCreatedEvent(dataSource));
//            Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
//            factory.setResources(new Resource[] { new ClassPathResource("data.json")});
//            return factory;
//        }
//    }
}
