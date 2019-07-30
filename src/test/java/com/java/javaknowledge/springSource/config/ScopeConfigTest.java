package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.Person;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScopeConfigTest {

    @Test
    public void person() {
        // ioc容器的启动
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ScopeConfig.class);

        String[] beanNamesForType = annotationConfigApplicationContext.getBeanNamesForType(Person.class);
        for (String name : beanNamesForType) {
            System.out.println(name);
        }

        // 测试注解@Scope 的功能，建立的是单例还是多例
        System.out.println("ioc容器创建完成...");
        Person person = annotationConfigApplicationContext.getBean(Person.class);
        Person person1 = annotationConfigApplicationContext.getBean(Person.class);
        // @Scope("prototype")时为多例，会创建不同的bean，输出false,
        // @Scope("singleton") 或者 @Scope 不设值的时候，为单例，输出true
        System.out.println(person == person1);
    }

}