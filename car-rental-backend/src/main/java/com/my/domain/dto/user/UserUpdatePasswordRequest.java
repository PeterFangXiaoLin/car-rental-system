package com.my.domain.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdatePasswordRequest implements Serializable {
    /**
     * 原密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
