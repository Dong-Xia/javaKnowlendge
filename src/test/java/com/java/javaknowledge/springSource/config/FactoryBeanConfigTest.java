package com.java.javaknowledge.springSource.config;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.*;

public class FactoryBeanConfigTest {

    @Test
    public void colorFactoryBean() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(FactoryBeanConfig.class);
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }

        // 获取工厂中colorFactoryBean时，默认是调用工厂bean中定义的getObject()方法返回创建的Bean到容器中
        Object colorFactoryBean = annotationConfigApplicationContext.getBean("colorFactoryBean");
        System.out.println(colorFactoryBean.toString());
        Object colorFactoryBean1 = annotationConfigApplicationContext.getBean("colorFactoryBean");
        System.out.println(colorFactoryBean1);
        // 测试是否为单例
        System.out.println(colorFactoryBean.equals(colorFactoryBean1));

        // 要获取工厂Bean本身，我们需要给id前面加一个&
        Object bean = annotationConfigApplicationContext.getBean("&colorFactoryBean");
        System.out.println(bean.getClass());
    }
}