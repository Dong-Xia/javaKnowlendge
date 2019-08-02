package com.java.javaknowledge.springSource.bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>Monkey<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/2 11:27<br/>
 * <b>@version：</b> 1.7.0.0 <br/>
 * <b>Copyright (c) 2019 ASPire Tech.</b>
 */
public class Monkey {
    private String name;

    public Monkey(){
        System.out.println("Monkey......construct......");
    }

    @PostConstruct
    public void init() {
        System.out.println("Monkey......init......");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Monkey......destroy......");
    }
}
