package com.java.javaknowledge.springSource.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * 使用FactoryBean工厂注册bean
 */
public class ColorFactoryBean implements FactoryBean<Blue> {

    /**
     * 返回一个对象，并添加注册到ioc容器中
     * @return
     * @throws Exception
     */
    @Override
    public Blue getObject() throws Exception {
        // 为单例模式，这个方法只会调用一次，只打印一次； 为多实例则会调用多次
        System.out.println("根据设置是否单例模式，判断调用创建新的Bean的次数：getObject......");
        return new Blue("蓝色的冰淇淋");
    }

    @Override
    public Class<?> getObjectType() {
        return Blue.class;
    }

    /**
     * 指定是否注册的bean为单例：
     *        1、 true ： 是单例，容器中保留一份
     *        2、 false:  每次调用都创建新的bean实例
     *
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
