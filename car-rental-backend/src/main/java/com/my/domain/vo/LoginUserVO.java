package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 已登录的用户视图
 */
@Data
public class LoginUserVO implements Serializable {
    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

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
     * 性别
     */
    private Integer gender;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 电子邮箱
     */
    private String email;

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

    /**
     * 创建时间
     */
    private Date createTime;

}
