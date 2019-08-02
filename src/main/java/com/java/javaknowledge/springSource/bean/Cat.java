package com.java.javaknowledge.springSource.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>Cat<br/>
 * <b>Description：通过Bean类实现InitializingBean(定义初始化逻辑)，DisposableBean(定义销毁逻辑)
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/2 11:05<br/>
 * <b>@version：</b> 1.7.0.0 <br/>
 * <b>Copyright (c) 2019 ASPire Tech.</b>
 */
@Component
public class Cat implements InitializingBean,DisposableBean {
    private String name;

    public Cat() {
        System.out.println("Cat.....construct......");
    }
    /**
     * 定义销毁逻辑
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("Cat......destroy......");
    }

    /**
     * 定义初始化逻辑:在对象创建后
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Cat......init......");
    }
}
