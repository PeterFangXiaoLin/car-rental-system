package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 司机表
 * @TableName driver
 */
@TableName(value ="driver")
@Data
public class Driver implements Serializable {
    /**
     * 司机ID
     */
    @TableId
    private Long id;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 头像URL
     */
    private String driverAvatar;

    /**
     * 性别：0-男，1-女
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 驾驶证号码
     */
    private String driverLicenseNo;

    /**
     * 驾照类型（A1/A2/B1/B2/C1等）
     */
    private String driverLicenseType;

    /**
     * 驾驶证照片URL
     */
    private String driverLicenseImg;

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
     * 日薪
     */
    private BigDecimal dailyPrice;

    /**
     * 工作状态：0-休息中，1-可接单，2-已接单
     */
    private Integer workStatus;

    /**
     * 评分（1-5分）
     */
    private BigDecimal rating;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

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