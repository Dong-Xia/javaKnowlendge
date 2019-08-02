package com.java.javaknowledge.springSource.bean;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>Dog<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/2 10:40<br/>
 * <b>@version：</b> 1.7.0.0 <br/>
 * <b>Copyright (c) 2019 ASPire Tech.</b>
 */
public class Dog {
    private String name;

    public Dog(){
        System.out.println("Dog......construct......");
    }

    public void init() {
        System.out.println("Dog......init......");
    }

    public void destroy() {
        System.out.println("Dog......destroy......");
    }
}
