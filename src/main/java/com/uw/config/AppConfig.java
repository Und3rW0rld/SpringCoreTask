package com.uw.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.uw")
public class AppConfig {

    @Bean
    public SessionFactory sessionFactory() {
        return new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build())
                .buildMetadata()
                .buildSessionFactory();
    }

}
