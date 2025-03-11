package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 认证记录表
 * @TableName verify_record
 */
@TableName(value ="verify_record")
@Data
public class VerifyRecord implements Serializable {
    /**
     * 记录ID
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 认证状态：0-未认证，1-认证中，2-已认证，3-认证失败
     */
    private Integer verifyStatus;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号码
     */
    private String idCardNumber;

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
     * 驾龄
     */
    private Integer drivingYears;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}