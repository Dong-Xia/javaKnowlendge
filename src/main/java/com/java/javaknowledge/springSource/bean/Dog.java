package com.java.javaknowledge.springSource.bean;

import org.springframework.beans.factory.annotation.Value;

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

    //1、直接命名
    @Value("旺财")
    private String name;

    //2、表达式
    @Value("#{10-2}")
    private Integer age;

    //3、读入配置外部配置文件
    @Value("${dog.nickname}")
    private String nickname;

    public Dog(){
        System.out.println("Dog......construct......");
    }

    public void init() {
        System.out.println("Dog......init......");
    }

    public void destroy() {
        System.out.println("Dog......destroy......");
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
