package com.my.domain.dto.driver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DriverQueryRequest extends PageRequest implements Serializable {

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 性别：0-男，1-女
     */
    private Integer gender;

    /**
     * 驾照类型（A1/A2/B1/B2/C1等）
     */
    private String driverLicenseType;

    /**
     * 最小日薪
     */
    private BigDecimal minPrice;

    /**
     * 最大日薪
     */
    private BigDecimal maxPrice;

    /**
     * 工作状态：0-休息中，1-可接单，2-已接单
     */
    private Integer workStatus;
}
