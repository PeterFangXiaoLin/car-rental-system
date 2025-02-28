package com.my.domain.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录 dto
 */
@Data
public class UserLoginRequest implements Serializable {
    private String userAccount;
    private String userPassword;
}
