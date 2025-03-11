package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

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
     * 性别: 0-男 1-女
     */
    private Integer gender;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号码
     */
    private String idCardNumber;

    /**
     * 实名认证状态：0-未实名，1-认证中，2-已认证，3-认证失败
     */
    private Integer verifyStatus;

    /**
     * 驾驶证号码
     */
    private String driverLicenseNo;

    /**
     * 驾照类型（A1/A2/B1/B2/C1等）
     */
    private String driverLicenseType;

    /**
     * 驾照发证日期
     */
    private Date driverLicenseIssueDate;

    /**
     * 驾照到期日期
     */
    private Date driverLicenseExpireDate;

    /**
     * 认证结果：0-待审核，1-通过，2-拒绝
     */
    private Integer verifyResult;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    /**
     * 认证时间
     */
    private Date verifyTime;

    /**
     * 审核人ID
     */
    private Long reviewId;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 会员等级: 0-普通用户，1-vip
     */
    private Integer memberLevel;

    /**
     * 用户角色：1-用户，2-司机，3-管理员
     */
    private Integer userRole;

    /**
     * 是否为司机：0-否，1-是
     */
    private Integer isDriver;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}