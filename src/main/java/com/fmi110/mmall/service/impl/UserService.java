package com.fmi110.mmall.service.impl;

import com.fmi110.mmall.commons.Const;
import com.fmi110.mmall.commons.ServerResponse;
import com.fmi110.mmall.commons.TokenCache;
import com.fmi110.mmall.dao.UserMapper;
import com.fmi110.mmall.pojo.User;
import com.fmi110.mmall.service.IUserService;
import com.fmi110.mmall.utils.DigestUtils;
import com.fmi110.mmall.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * @author fmi110
 * @Description:
 * @Date 2018/2/9 14:57
 */
@Service
@Slf4j
@Transactional(readOnly = true) // 全局只读事务,写事务在方法上单独注解!!!
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;

    /**
     * 登录
     * <ol>
     * <li>校验用户名是否存在</li>
     * <li>检验用户名和密码是否正确</li>
     * </ol>
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {

        int count = userMapper.checkUsername(username);
        if (count == 0) {
            log.info("用户名不存在,{}", JsonUtils.toJson(username));
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        // 加密密码
        password = DigestUtils.md5Hex(password);
        User user = userMapper.login(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        // 清除密码回传!!!
        user.setPassword(null);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    /**
     * 注册
     * <ol>
     *     <li>检验用户名是否已存在</li>
     *     <li>校验邮箱是否已存在</li>
     *     <li>用户密码加密处理</li>
     *     <li>保存用户到数据库</li>
     * </ol>
     * @param user
     * @return
     */
    @Transactional
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> response = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!response.isSuccess()) {
            return response;
        }
        ServerResponse<String> res = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!res.isSuccess()) {
            return res;
        }
        /**
         * todo:密码加密处理
         */
        // 密码做 md5 加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        int insertCount = userMapper.insert(user);
        if (insertCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 检查用户名或者邮箱是否有效
     *
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (StringUtils.equals(Const.USERNAME, type)) {
                int count = userMapper.checkUsername(str);
                if (count > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            } else if (StringUtils.equals(Const.EMAIL, type)) {
                if (userMapper.checkEmail(str) > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        log.warn("方法参数不对 : checkValid({}, {})", str, type);
        return ServerResponse.createBySuccessMessage("数据可以使用");
    }

    /**
     * 忘记密码的提示问题
     * <ol>
     *     <li>校验用户名是否存在</li>
     *     <li>根据用户名获取问题</li>
     * </ol>
     * @param username
     * @return
     */
    @Override
    public ServerResponse selectQuestion(String username) {

        ServerResponse<String> response = this.checkValid(username, Const.USERNAME); //
        if (response.isSuccess()) { // 数据合法,说明用户名不存在.返回
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String question = userMapper.selectQuestion(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("未设置密码问题");
    }

    /**
     * 检查忘记密码问题答案是否正确
     *  <ol>
     *      <li>验证问题答案是否正确</li>
     *      <li>随机生成一个 token 字符串,用于修改密码时校验</li>
     *  </ol>
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int count = userMapper.checkAnswer(username, question, answer);
        if (count == 0) {
            return ServerResponse.createByErrorMessage("问题答案错误");
        }
        // 生成 token , 用户修改密码时验证 , 并且需要返回给客户端使用
        String token = UUID.randomUUID()
                           .toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, token);
        return ServerResponse.createBySuccess(token);
    }

    /**
     * 忘记密码的重置密码
     * <ol>
     *     <li>校验前端传的token不为空</li>
     *     <li>校验前端传的token和后台的token是否一致</li>
     *     <li>加密前端传输的密码,然后更新密码</li>
     * </ol>
     * @param username
     * @param passwordNew
     * @param forgetToken 忘记密码的问题的答案
     * @return
     */
    @Transactional
    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (!StringUtils.isNotBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误,token为空");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (!StringUtils.equals(token, forgetToken)) {
            return ServerResponse.createByErrorMessage("token无效或过期,请重新获取");
        }
        if (this.checkValid(username, Const.USERNAME)
                 .isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        /**
         * todo 密码加密
         */
        passwordNew = DigestUtils.md5Hex(passwordNew);
        int count = userMapper.updatePasswordByUsername(username, passwordNew);
        if (count > 0) {
            return ServerResponse.createBySuccess("密码修改成功");
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }

    /**
     * 登录状态下重置密码
     * <ol>
     *     <li>检验旧密码正确,防止横向越权</li>
     *     <li>加密密码,然后更新</li>
     * </ol>
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    @Transactional
    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        // 先校验密码
        if (userMapper.checkPassword(user.getUsername(), DigestUtils.md5Hex(passwordOld)) == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        if (userMapper.updatePasswordByUsername(DigestUtils.md5Hex(passwordNew), user.getUsername()) > 0) {
            return ServerResponse.createBySuccess("密码修改成功");
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }

    /**
     * 更新用户信息 , 这里业务设定为 : 不修改用户名,不修改密码
     * <ol>
     *     <li>校验用户是否存在</li>
     *     <li>校验新邮箱是否可用</li>
     *     <li>更新用户信息</li>
     * </ol>
     * @param user
     * @return
     */
    @Transactional
    @Override
    public ServerResponse<User> updateInformation(User user) {
        // 用户名不修改,密码不修改
        // 需要验证邮箱
        if (!this.checkValid(user.getEmail(), Const.EMAIL)
                 .isSuccess()) {
            return ServerResponse.createByErrorMessage("邮箱已经存在");
        }
        /**
         * 新建一个用户,填充要修改的字段,防止误修改
         */
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setEmail(user.getEmail());
        if (StringUtils.isNotBlank(user.getPhone()))
            updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setUpdateTime(new Date());

        if (userMapper.updateByPrimaryKeySelective(updateUser) > 0) {
            return ServerResponse.createBySuccessMessage("信息修改成功");
        }
        return ServerResponse.createByErrorMessage("信息修改失败");
    }

    /**
     * 获取用户信息
     * <ol>
     *     <li>用户不存在时,提示不存在</li>
     *     <li>清除用户密码,返回用户信息</li>
     * </ol>
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        // 清空密码
        user.setPassword(null);

        return ServerResponse.createBySuccess(user);
    }

    /**
     * 校验是否是管理员
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole()
                                .intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
