package com.fmi110.test;

import com.fmi110.mmall.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author fmi110
 * @Description:
 * @Date 2018/2/7 21:05
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application*.xml")
public class UserServiceTest {

    @Autowired
    IUserService userService;

    @Test
    public void testLog(){
//        ServerResponse<String> response = userService.checkValid("fmi110", Const.USERNAME);
//        System.out.println(JsonUtils.toJson(response));

        userService.login("admin", "admin");
    }
}
