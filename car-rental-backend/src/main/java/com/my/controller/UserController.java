package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.user.*;
import com.my.domain.entity.User;
import com.my.domain.vo.LoginUserVO;
import com.my.domain.vo.UserVO;
import com.my.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        return success(userService.userRegister(userRegisterRequest));
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        return success(userService.userLogin(userLoginRequest, request));
    }

    @GetMapping("/get/login")
    @ApiOperation(value = "获取当前登录用户")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        return success(userService.getLoginUser(request));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户注销（退出登录）")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        return success(userService.userLogout(request));
    }

    @PostMapping("/update/password")
    @ApiOperation(value = "重设密码")
    public BaseResponse<Boolean> updateUserPassword(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest, HttpServletRequest request) {
        return success(userService.updateUserPassword(userUpdatePasswordRequest, request));
    }

    @PostMapping("/update/info")
    @ApiOperation(value = "更新用户信息")
    public BaseResponse<LoginUserVO> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        return success(userService.updateUser(userUpdateRequest, request));
    }

    @PostMapping("/update/avatar")
    @ApiOperation(value = "修改头像")
    public BaseResponse<String> updateAvatar(@RequestPart("file")MultipartFile file, HttpServletRequest request) {
        return success(userService.updateAvatar(file, request));
    }

    @PostMapping("/admin/add")
    @ApiOperation(value = "添加用户 (管理员)")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        return success(userService.addUser(userAddRequest));
    }

    @PostMapping("/admin/update")
    @ApiOperation(value = "更新用户（仅管理员）")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> adminUpdateUser(@RequestBody UserAdminUpdateRequest userAdminUpdateRequest) {
        return success(userService.adminUpdateUser(userAdminUpdateRequest));
    }

    @PostMapping("/admin/delete")
    @ApiOperation(value = "删除用户")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> adminDeleteUser(@RequestBody DeleteRequest deleteRequest) {
        return success(userService.adminDeleteUser(deleteRequest));
    }

    @PostMapping("/admin/list/vo")
    @ApiOperation(value = "获取用户列表（仅管理员）")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> pageUserVO(@RequestBody UserQueryRequest userQueryRequest) {
        return success(userService.pageUserVO(userQueryRequest));
    }

    @GetMapping("/get/vo")
    @ApiOperation(value = "根据id获取用户信息")
    public BaseResponse<UserVO> getUserById(@RequestParam("id") Long id) {
        return success(userService.getUserById(id));
    }

    @GetMapping("/get")
    @ApiOperation(value = "根据id获取用户（管理员）")
    public BaseResponse<User> getUser(@RequestParam("id") Long id) {
        return success(userService.getUser(id));
    }
}
