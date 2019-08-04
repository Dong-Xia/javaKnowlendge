package com.java.javaknowledge.springSource.config;

import com.java.javaknowledge.springSource.dao.PersonDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>AutowiredConfig<br/>
 * <b>Description：自动装配@Autowired注解：
 *     自动装配： spring利用依赖注入（DI）,完成对IOC容器中各个组件的依赖关系赋值；
 *       1）、@Autowired : 自动注入；
 *               a)、默认优先按照类型去容器中找对应的组件： PersonDao.class
 *               b)、当按照类型去IOC容器中找到有多个相同类型的组件时，则将属性名称作为组件的id去容器中去找：private PersonDao personDao中的personDao作为标准
 *      2）、采用@Qualifier : 如果ioc容器中存在相同类型的组件时，可以使用该注解去实现寻找指定的id名称的组件，而不是像上面b中使用属性名
 *      3）、自动装配的前提：IOC容器中必须提前将装配所需的bean准备好，不然一运行装配则会报没有找到定义的bean错误：NoSuchBeanDefinitionException
 *      4)、@Primary：
 *               a)、在没有使用@Qualifier注解时，IOC容器中有多个相同类型的组件，则会优先加载该注解的组件，不会按照属性名称去装配
 *               b)、在使用@Qualifier注解时，则该注解无效，装配优先按照@Qualifier("personDao")注解指定的组件装配
 *
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/3 16:43<br/>
 */

@Configuration
@ComponentScan(value = {"com.java.javaknowledge.springSource.dao","com.java.javaknowledge.springSource.service"})
public class AutowiredConfig {

    /**
     *  @Primary：
     *       a)、在没有使用@Qualifier注解时，IOC容器中有多个相同类型的组件，则会优先加载该注解的组件，不会按照属性名称去装配
     *       b)、在使用@Qualifier注解时，则该注解无效，装配优先按照@Qualifier("personDao")注解指定的组件装配
     * @return
     */
    @Primary
    @Bean("personDao1")
    public PersonDao personDao(){
        PersonDao personDao = new PersonDao();
        personDao.setName("风清扬");
        return personDao;
    }
}
