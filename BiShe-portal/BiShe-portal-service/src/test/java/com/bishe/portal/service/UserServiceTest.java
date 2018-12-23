package com.bishe.portal.service;

import com.bishe.portal.model.po.TbUsersPo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author:GaoPan
 * @Date:2018/12/19 22:13
 * @Version 1.0
 **/
public class UserServiceTest {
    private ApplicationContext applicationContext;

    @Before
    public void before() {
        String[] strings = new String[]{"spring/spring-portal-service.xml",
                "spring/spring-portal-dao.xml"};
        applicationContext = new ClassPathXmlApplicationContext(strings);
    }

    @Test
    public void testInsert(){
        UserService userService = applicationContext.getBean("userServiceImpl", UserService.class);
        TbUsersPo tbUsersPo = new TbUsersPo();
        tbUsersPo.setPwd("123");
        tbUsersPo.setName("pan");
        userService.enroll(tbUsersPo);
    }

    @Test
    public void testLogin(){
        UserService userService = applicationContext.getBean("userServiceImpl", UserService.class);
        boolean tag = userService.login("pan", "13");
        System.out.println(tag);
    }
}
