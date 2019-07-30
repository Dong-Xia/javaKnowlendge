package com.java.javaknowledge.springSource.test;

import com.java.javaknowledge.springSource.bean.Person;
import com.java.javaknowledge.springSource.config.MainConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class mainTest {
    public static void main(String[] args) {
        // 使用xml方式进行注册bean
/*        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person.toString());*/

        // 使用注解的方式注册使用bean
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        Person person = annotationConfigApplicationContext.getBean(Person.class);
        System.out.println(person);

        // 获取id的名称
        String[] beanNamesForType = annotationConfigApplicationContext.getBeanNamesForType(Person.class);
        for (String name : beanNamesForType) {
            System.out.println(name);
        }
    }
}
