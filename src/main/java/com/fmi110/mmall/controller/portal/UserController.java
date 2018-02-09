package com.fmi110.mmall.controller.portal;

import com.fmi110.mmall.pojo.User;
import com.fmi110.mmall.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author fmi110
 * @Description: 用户模块
 * @Date 2018/2/9 17:30
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping("/login")
    public Object login(String username, String password, HttpSession session) {
        return null;
    }

    @PostMapping("/register")
    public Object register(User user) {
        return null;
    }

    @PostMapping("/check_valid")
    public Object checkValid(String data, String type) {
        return null;
    }

    @PostMapping("get_user_info")
    public Object getUserInfo(HttpSession session) {
        return null;
    }

    @PostMapping("forget_get_question")
    public Object forgetGetQuestion(String username) {
        return null;
    }

    @PostMapping("forget_check_answer")
    public Object forgetCheckAnswer(String username, String password, String answer) {
        return null;
    }

    @PostMapping("reset_password")
    public Object resetPwd(HttpSession session, String passwordOld, String passwordNew) {
        return null;
    }

    @PostMapping("update_information")
    public Object updateInformation(User user) {
        return null;
    }

    @RequestMapping("logout")
    public Object logout(HttpSession httpsession) {
        return null;
    }
}
