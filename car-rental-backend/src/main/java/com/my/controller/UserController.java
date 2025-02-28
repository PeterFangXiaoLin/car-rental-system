package com.my.controller;

import com.my.common.BaseResponse;
import com.my.domain.dto.user.UserLoginRequest;
import com.my.domain.dto.user.UserRegisterRequest;
import com.my.domain.dto.user.UserUpdatePasswordRequest;
import com.my.domain.dto.user.UserUpdateRequest;
import com.my.domain.vo.LoginUserVO;
import com.my.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
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
}
