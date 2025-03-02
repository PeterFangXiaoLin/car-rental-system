package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图对象
 */
@Data
public class UserVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 账号
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

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
} 