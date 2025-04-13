package com.my.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.DeleteRequest;
import com.my.domain.dto.user.*;
import com.my.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.vo.LoginUserVO;
import com.my.domain.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-02-27 11:50:27
*/
public interface UserService extends IService<User> {

    /**
     * 用户信息校验
     * @param user
     * @param isAdd
     */
    void validate(User user, boolean isAdd);

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
    UserVO updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

    /**
     * 修改头像
     *
     * @param file
     * @param request
     * @return
     */
    String updateAvatar(MultipartFile file, HttpServletRequest request);

    /**
     * 添加用户
     * @param userAddRequest
     * @return
     */
    Long addUser(UserAddRequest userAddRequest);

    /**
     * 管理员更新用户信息
     *
     * @param userAdminUpdateRequest
     * @return
     */
    Boolean adminUpdateUser(UserAdminUpdateRequest userAdminUpdateRequest);

    /**
     * 管理员删除用户
     * @param deleteRequest
     * @return
     */
    Boolean adminDeleteUser(DeleteRequest deleteRequest);

    /**
     * 管理员获取用户列表
     *
     * @param userQueryRequest
     * @return
     */
    Page<UserVO> pageUserVO(UserQueryRequest userQueryRequest);

    /**
     * 获取查询 Wrapper
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 获取脱敏后的用户
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);


    /**
     * 获取脱敏后的用户列表
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    UserVO getUserById(Long id);

    /**
     * 根据id获取用户
     *
     * @param id
     * @return
     */
    User getUser(Long id);

}
