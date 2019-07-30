package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.Person;
import com.java.javaknowledge.springSource.condition.LinuxConditional;
import com.java.javaknowledge.springSource.condition.WindowsConditional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 学习@Conditional注解的配置类: 当满足条件才会在ioc容器中创建bean
 */
@Configuration
// Conditional用在类上，表示需满足需求才加载类中的方法
//@Conditional(LinuxConditional.class)
public class ConditionalConfig {
    /**
     * 学习Conditional注解：
     *  当使用的系统为Windows系统，则在ioc容器中创建id = person01的bean
     *
     */
    @Conditional(WindowsConditional.class)
    @Bean
    public Person person01(){
        return new Person("Bill Gates", 80);
    }

    /**
     * 当使用的系统为linux系统，则在ioc容器中创建id = person02的bean
     * @return
     */
    @Conditional(LinuxConditional.class)
    @Bean
    public Person person02() {
        return new Person("Linux", 50);
    }
}
