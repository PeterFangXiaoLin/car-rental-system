package com.my.domain.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号码
     */
    private String idCardNumber;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 驾驶证号码
     */
    private String drivingLicenseNo;

    /**
     * 驾龄
     */
    private Integer drivingYears;
}
