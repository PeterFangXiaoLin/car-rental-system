package com.my.domain.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddRequest implements Serializable {
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

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

    /**
     * 信用评分
     */
    private Integer creditScore;

    /**
     * 会员等级: 0-普通用户，1-vip
     */
    private Integer memberLevel;

    /**
     * 用户角色：0-用户，1-司机，2-管理员
     */
    private Integer userRole;
}
