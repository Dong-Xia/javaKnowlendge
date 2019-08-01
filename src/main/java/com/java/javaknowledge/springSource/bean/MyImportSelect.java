package com.java.javaknowledge.springSource.bean;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * ImportSelector：能设置引入多个Bean对象，将这些类注册到ioc容器中，需要使用在配置类上使用@Import注解引入
 */
public class MyImportSelect implements ImportSelector {

    /**
     * AnnotationMetadata: 当前标注@Import的注解类的注解信息
     * @param annotationMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
        for (String a: annotationTypes) {
            System.out.println(a);
        }
        return new String[]{"com.java.javaknowledge.springSource.bean.Blue","com.java.javaknowledge.springSource.bean.Red"};
    }
}
