package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.Person;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

import static org.junit.Assert.*;

public class ConditionalConfigTest {
    AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ConditionalConfig.class);

    @Test
    public void person01() {
        // 获取ioc容器中的bean名
        String[] beanNamesForType = annotationConfigApplicationContext.getBeanNamesForType(Person.class);
        ConfigurableEnvironment environment = annotationConfigApplicationContext.getEnvironment();
        // 获取使用的操作系统名称
        String property = environment.getProperty("os.name");
        System.out.println(property);
        for (String name : beanNamesForType) {
            System.out.println(name);
        }
        // 获取beans实体
        Map<String, Person> beansOfType = annotationConfigApplicationContext.getBeansOfType(Person.class);
        System.out.println(beansOfType);
    }
}