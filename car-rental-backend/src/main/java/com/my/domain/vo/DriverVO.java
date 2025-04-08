package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DriverVO implements Serializable {
    /**
     * 司机ID
     */
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
     * 创建时间
     */
    private Date createTime;
}
