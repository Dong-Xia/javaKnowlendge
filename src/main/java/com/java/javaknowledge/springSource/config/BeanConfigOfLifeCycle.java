package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.Cat;
import com.java.javaknowledge.springSource.bean.Dog;
import com.java.javaknowledge.springSource.bean.Monkey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>BeanConfigOfLifeCycle<br/>
 * <b>Description：Bean的生命周期： bean创建---初始化---销毁的过程
 *                 容器管理bean的生命周期： 我们可以自定义初始化和销毁方法，容器在bean进行到当前生命周期的时候来调用我们自定义的初始化和销毁方法
 *
 *      对象创建：
 *          1）、单实例：在容器启动的时候创建对象
 *          2）、多实例：在每次获取的bean的时候创建对象,只执行初始化方法，不执行销毁方法
 *
 *      对象初始化和销毁具体实现有以下几种：
 *                          1）、在对象中指定初始化和销毁方法：
 *                              在@Bean注解中指定initMethod和destroyMethod
 *                          2）、通过Bean类实现InitializingBean(定义初始化逻辑)，DisposableBean(定义销毁逻辑)
 *                          3）、在Bean类初始化方法上添加@PostConstruct和销毁方法上添加@PreDestroy
 *                              a)、@PostConstruct: 在bean创建完成并且属性赋值完成后，来执行初始化方法
 *                              b)、@PreDestroy:在容器销毁bean对象之前通知我们进行清理工作
 *                          4）、通过Bean类实现BeanPostProcessor后置处理器，实现bean初始化前后做一些操作
 *                              a)、 postProcessBeforeInitialization方法：在初始化前做一些操作
 *                              b)、 postProcessAfterInitialization方法：在初始化后做一些操作
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/2 10:30<br/>
 */
@Configuration
@ComponentScan("com.java.javaknowledge.springSource.bean")
public class BeanConfigOfLifeCycle {

    /**
     * 1、在@Bean注解中指定initMethod和destroyMethod
     * @return
     */
    @Bean(initMethod = "init",destroyMethod = "destroy")
    public Dog dog() {
        return new Dog();
    }

    /**
     * 2、通过Bean类实现InitializingBean(定义初始化逻辑)，DisposableBean(定义销毁逻辑)，具体实现看Cat.java类
     * @return
     */
    @Bean
    public Cat cat() {
        return new Cat();
    }

    /**
     * 3、在Bean类初始化方法上添加@PostConstruct和销毁方法上添加@PreDestroy
     *                             a)、@PostConstruct: 在bean创建完成并且属性赋值完成后，来执行初始化方法
     *                             b)、@PreDestroy:在容器销毁bean对象之前通知我们进行清理工作
     * @return
     */
    @Bean
    public Monkey monkey() {
        return new Monkey();
    }
}
