package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.bean.Dog;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.*;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>PropertyValueConfigTest<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/3 15:35<br/>
 */
public class PropertyValueConfigTest {

    @Test
    public void dog() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PropertyValueConfig.class);
        Dog bean = annotationConfigApplicationContext.getBean(Dog.class);
        System.out.println(bean);
    }
}