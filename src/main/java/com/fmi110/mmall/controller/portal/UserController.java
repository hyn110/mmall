package com.fmi110.mmall.controller.portal;

import com.fmi110.mmall.commons.Const;
import com.fmi110.mmall.commons.ServerResponse;
import com.fmi110.mmall.pojo.User;
import com.fmi110.mmall.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * @author fmi110
 * @Description: 用户模块
 * @Date 2018/2/9 17:30
 */
@Api(description = "用户模块-门户")
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {
    @Autowired
    IUserService userService;

    @ApiOperation(value = "登录", httpMethod = "POST", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({
                               @ApiImplicitParam(name = "username", value = "用户名", paramType = "form"),
                               @ApiImplicitParam(name = "password", value = "用户密码", paramType = "form")
                       })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse login(String username, String password, @ApiIgnore HttpSession session) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @ApiOperation(value = "注册")
    @ApiImplicitParam(name = "user", value = "用户信息实体", dataType = "User")
    @PostMapping("/register")
    public ServerResponse register(@ModelAttribute User user) {
        /**
         * todo : 使用数据校验,限定字段不能为空  validate
         */
        return userService.register(user);
    }

    @ApiOperation(value = "检验数据是否可用")
    @ApiImplicitParams({
                               @ApiImplicitParam(name = "data", value = "要检验的数据", paramType = "form"),
                               @ApiImplicitParam(name = "type", value = "username 或 email", paramType = "form")
                       })
    @PostMapping("/check_valid")
    public ServerResponse checkValid(String data, String type) {
        return userService.checkValid(data, type);
    }

    @ApiOperation(value = "获取用户信息")
    @PostMapping("get_user_info")
    public ServerResponse getUserInfo(@ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return userService.getInformation(user.getId());
        }
        return ServerResponse.createByErrorMessage("用户未登录");
    }

    @ApiOperation(value = "获取忘记密码设置的问题")
    @ApiImplicitParam(name = "username", value = "用户名", paramType = "form")
    @PostMapping("forget_get_question")
    public ServerResponse forgetGetQuestion(String username) {
        return userService.selectQuestion(username);
    }

    @ApiOperation(value = "校验问题答案是否正确")
    @ApiImplicitParams({
                               @ApiImplicitParam(name = "username", value = "用户名", paramType = "form"),
                               @ApiImplicitParam(name = "question", value = "忘记密码的问题", paramType = "form"),
                               @ApiImplicitParam(name = "answer", value = "忘记密码的问题的答案", paramType = "form")
                       })
    @PostMapping("forget_check_answer")
    public ServerResponse forgetCheckAnswer(String username,
                                            String question,
                                            String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @ApiOperation(value = "忘记密码情况下的重置密码")
    @ApiImplicitParams({
                               @ApiImplicitParam(name = "username", value = "用户名", paramType = "form"),
                               @ApiImplicitParam(name = "passwordNew", value = "新密码", paramType = "form"),
                               @ApiImplicitParam(name = "forgetToken", value = "验证忘记问题答案正确后,后台返回的token",
                                                 paramType = "form")
                       })
    public ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return userService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @ApiOperation(value = "登录状态下重置密码")
    @ApiImplicitParams({
                               @ApiImplicitParam(name = "passwordOld", value = "旧密码", paramType = "form"),
                               @ApiImplicitParam(name = "passwordNew", value = "新密码", paramType = "form")

                       })
    @PostMapping("reset_password")
    public ServerResponse resetPwd(@ApiIgnore HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录,请先登录");
        }
        return userService.resetPassword(passwordOld, passwordNew, user);
    }

    @ApiOperation(value = "更新用户信息")
    @ApiImplicitParam(name = "user", value = "用户信息实体", dataType = "User")
    @PostMapping("update_information")
    public ServerResponse updateInformation(@ApiIgnore HttpSession session, @ModelAttribute User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录,请先登录");
        }
        // 不修改名字和id
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        return userService.updateInformation(user);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("logout")
    public ServerResponse logout(@ApiIgnore HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出成功");
    }

    @ApiOperation(value = "单纯的测试用,为了让swagger2能显示对象的结构!!!")
    @GetMapping("zzz")
    public User test() {
        return new User();
    }
}
