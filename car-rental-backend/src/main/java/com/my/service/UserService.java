package com.my.service;

import com.my.domain.dto.UserRegisterRequest;
import com.my.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-02-27 11:50:27
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);
}
