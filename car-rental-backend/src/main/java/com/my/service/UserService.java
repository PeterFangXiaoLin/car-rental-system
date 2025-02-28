package com.my.service;

import com.my.domain.dto.user.UserLoginRequest;
import com.my.domain.dto.user.UserRegisterRequest;
import com.my.domain.dto.user.UserUpdatePasswordRequest;
import com.my.domain.dto.user.UserUpdateRequest;
import com.my.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.vo.LoginUserVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-02-27 11:50:27
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    LoginUserVO getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    Boolean userLogout(HttpServletRequest request);

    /**
     * 是否是管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否是管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 修改密码
     *
     * @param userUpdatePasswordRequest
     * @param request
     * @return
     */
    boolean updateUserPassword(UserUpdatePasswordRequest userUpdatePasswordRequest, HttpServletRequest request);

    /**
     * 修改个人信息
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    LoginUserVO updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

    /**
     * 修改头像
     *
     * @param file
     * @param request
     * @return
     */
    String updateAvatar(MultipartFile file, HttpServletRequest request);
}
