package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.Person;
import org.springframework.context.annotation.*;

@Configuration
public class ScopeConfig {

    /**
     *         // ioc容器的启动
     *         AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ScopeConfig.class);
     *
     * Scope ： 1、singleton ：单例模式(默认值)-（默认加载方式为：恶汉式,容器一加载则创建对象）。 ioc容器启动就立即调用方法person()创建对象放到ioc容器中，以后无论获取多少次都是
     *                                           直接从ioc容器中拿，不会再去调用方法，换句话说创建对象的方法只会调用一次，
     *                                           ioc容器中始终只有一份实例，故为单例。
     *          2、prototype ：多实例模式。  ioc容器启动时并不会去调用方法创建对象放在容器中，每次获取bean的时候才会调用person()方法去
     *                                      建立对象，并且没调用 一次则会建立一个新的对象，故为多实例。
     * @return
     */
    @Scope("prototype")   // 可以设置该bean实例初始化进ioc容器中是以单例模式存在还是多实例存在，默认为单例模式。 具体解析见方法注解
    //@Lazy   // 懒加载（懒汉式）： 1、主要针对Scope为单例模式下的；当Scope设置为"prototype"时，没有作用。
            //                   2、 ioc容器启动时不会创建对象，只有在第一次获取bean的时候才会去创建对象。
    @Bean("xiadong")
    public Person person(){
        System.out.println("给容器中添加新的对象...");
        return new Person("吓",29);
    }
}
