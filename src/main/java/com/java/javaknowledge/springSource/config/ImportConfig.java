package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.MyImportBeanDefinitionRegistrar;
import com.java.javaknowledge.springSource.bean.MyImportSelect;
import com.java.javaknowledge.springSource.bean.Person;
import com.java.javaknowledge.springSource.bean.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 学习Import注解: 其作用和@Bean一样，快速向容器注册一个组件
 *          1. @Import(要导入到容器的组件) ： 容器中就会自动注册这个组件，bean的id默认为全类名
 *          2. 实现 ImportSelector接口，可以一次引入多个bean组件，通过返回一个需要导入的组件的全类名数组
 *          3. 实现 ImportBeanDefinitionRegistrar 接口： 手动注册满足条件的bean到容器中
 */
@Configuration
@Import({Student.class,MyImportSelect.class,MyImportBeanDefinitionRegistrar.class})
public class ImportConfig {

    @Bean("xia")
    public Person person(){
        System.out.println("给容器中添加新的对象person...");
        return new Person("吓",29);
    }

    @Bean("dong")
    public Person person01(){
        System.out.println("给容器中添加新的对象person01...");
        return new Person("吓",29);
    }
}
