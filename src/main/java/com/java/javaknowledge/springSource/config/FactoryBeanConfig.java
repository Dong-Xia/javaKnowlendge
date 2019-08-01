package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.ColorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习以FactoryBean工厂方式向容器注册一个组件，完成Bean的注册
 *
 */
@Configuration
public class FactoryBeanConfig {

    /**
     * 向容器中注册工厂bean : colorFactoryBean
     *
     * @return
     */
    @Bean
    public ColorFactoryBean colorFactoryBean(){
        return new ColorFactoryBean();
    }
}
