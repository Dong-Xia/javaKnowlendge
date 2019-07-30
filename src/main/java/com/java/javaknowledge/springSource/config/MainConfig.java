package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

// 配置类的效果  和  xml 是同样的功效
@Configuration    // 注明该类为配置类,告诉spring这是一个配置类
@ComponentScan(value = "com.java.javaknowledge.springSource",excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class,Service.class})
})      // ComponentScan ：配置需要扫描的包
        // excludeFilters = Filter[] :为指定扫描排除哪些组件
        // includeFilters = Filter[] :为指定扫描包含哪些组件
public class MainConfig {

    // 给spring容器中注册一个Bean，对应的xml中的id默认为方法名
    @Bean("person")  // 括号中可以指定id的名称，如果这里指定则方法名不起作用
    public Person person1(){
        return new Person("李四", 26);
    }
}
