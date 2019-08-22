package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.aop.Sum;
import com.java.javaknowledge.springSource.bean.aop.SumAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>AopConfig<br/>
 * <b>Description：</b>spring aop配置类<br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/19 17:40<br/>
 *
 *    AOP 原理：必须实现下面的注解，才能开启AOP
 *      1、@EnableAspectJAutoproxy : @Import(AspectJAutoProxyRegistrar.class)，给容器中导入AspectJAutoProxyRegistrar，
 *      利用AspectJAutoProxyRegistrar自定义给容器中注入bean，
 *      该注解底层需引入组件AnnotationAwareAspectJAutoProxyCreator（注解注入切面自动代理创造器）
 *                          ->AspectJAwareAdvisorAutoProxyCreator
 *                             ->AbstractAdvisorAutoProxyCreator
 *                               ->AbstractAutoProxyCreator
 *                                 ->implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
 *     2、该组件如上最终继承实现了SmartInstantiationAwareBeanPostProcessor（bean后置处理器:用于bean生命周期中初始化前后需要做的事）和BeanFactoryAware（bean工厂注入）
 *     3、
 */
@EnableAspectJAutoProxy
@Configuration
public class AopConfig {

    @Bean
    public Sum sum() {
        return new Sum();
    }

    @Bean
    public SumAop sumAop() {
        return new SumAop();
    }
}
