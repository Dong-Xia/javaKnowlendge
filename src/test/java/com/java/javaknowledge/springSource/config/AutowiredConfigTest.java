package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.service.PersonService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>AutowiredConfigTest<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/3 16:51<br/>
 * <b>@version：</b> 1.7.0.0 <br/>
 * <b>Copyright (c) 2019 ASPire Tech.</b>
 */
public class AutowiredConfigTest {

    @Test
    public void personDao() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AutowiredConfig.class);
        PersonService bean = annotationConfigApplicationContext.getBean(PersonService.class);
        bean.printPerson();
    }
}