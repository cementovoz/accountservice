package com.bitbucket.pelenthium.accountservice;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by cementovoz on 20.08.14.
 */
@Configuration
@ComponentScan(basePackages = {"com.bitbucket.pelenthium.accountservice"})
@ImportResource(value = "classpath:/com/bitbucket/pelenthium/accountservice/applicationContext.xml")
public class JavaTestConfig {


    @Bean
    public PropertyPlaceholderConfigurer getPropertyConfigurer () {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("/com/bitbucket/pelenthium/accountservice/test.properties"));
        return configurer;
    }
}
