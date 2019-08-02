package com.java.javaknowledge.springSource.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>MyBeanPostProcessor<br/>
 * <b>Description：实现BeanPostProcessor：bean的后置处理器，实现bean初始化前后做一些操作
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/2 14:02<br/>
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    /**
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化前......."+ beanName + "---------->"+ bean);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化后......."+ beanName + "---------->"+ bean);
        return null;
    }
}
