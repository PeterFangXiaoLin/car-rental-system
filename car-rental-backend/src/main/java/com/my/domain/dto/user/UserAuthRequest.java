package com.my.domain.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserAuthRequest implements Serializable {
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

}
