package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.Dog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>PropertyValueConfig<br/>
 * <b>Description：@Value注解的使用： 给成员属性赋值,在具体的类中的成员属性上使用注解实现
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/3 15:23<br/>
 */

// 使用@PropertySource引入外部配置文件到环境变量中
@PropertySource(value="classpath:/dog1.properties",encoding="UTF-8")
@Configuration
public class PropertyValueConfig {

    @Bean
    public Dog dog(){
        return new Dog();
    }

}
