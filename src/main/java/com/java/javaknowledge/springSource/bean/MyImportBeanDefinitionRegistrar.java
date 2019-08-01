package com.java.javaknowledge.springSource.bean;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ImportBeanDefinitionRegistrar : 手动注册bean到容器中，需要使用在配置类上使用@Import注解引入
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     *
     * @param annotationMetadata : 当前类的注解信息
     * @param beanDefinitionRegistry ：可以获取已经注册到ioc容器中的bean信息，并且可以调用
     *                             beanDefinitionRegistry.registerBeanDefinition()方法实现新的bean注册到ioc容器中
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        boolean b = beanDefinitionRegistry.containsBeanDefinition("com.java.javaknowledge.springSource.bean.Blue");
        boolean r = beanDefinitionRegistry.containsBeanDefinition("com.java.javaknowledge.springSource.bean.Red");

        // 满足容器中有Blue实体和Red实体条件才注册新的实体类
        if (b && r) {
            //指定Bean的定义信息:Bean指向的所属类
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(RainBow.class);
            // 指定注册的Bean的id名
            beanDefinitionRegistry.registerBeanDefinition("rainBow",rootBeanDefinition);
        }

    }
}
