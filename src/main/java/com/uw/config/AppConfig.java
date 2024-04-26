package com.uw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan("com.uw")
public class AppConfig {

    @Bean
    @Scope("prototype")
    public Map<Long, Object> myStorage() {
        return new HashMap<>();
    }

}
