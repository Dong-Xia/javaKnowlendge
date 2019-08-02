package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.Cat;
import com.java.javaknowledge.springSource.bean.Dog;
import com.java.javaknowledge.springSource.bean.Monkey;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.*;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>BeanConfigOfLifeCycleTest<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/2 10:50<br/>
 * <b>@version：</b> 1.7.0.0 <br/>
 * <b>Copyright (c) 2019 ASPire Tech.</b>
 */
public class BeanConfigOfLifeCycleTest {

    @Test
    public void dog() throws Exception {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(BeanConfigOfLifeCycle.class);
        Dog bean = annotationConfigApplicationContext.getBean(Dog.class);
        // 销毁bean
        bean.destroy();

        Cat cat = annotationConfigApplicationContext.getBean(Cat.class);
        cat.destroy();

        Monkey monkey = annotationConfigApplicationContext.getBean(Monkey.class);
        monkey.destroy();
    }
}