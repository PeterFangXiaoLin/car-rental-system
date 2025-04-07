package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆型号表
 * @TableName vehicle_model
 */
@Data
public class VehicleModelVO implements Serializable {
    /**
     * 型号ID
     */
    private Long id;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 型号logo
     */
    private String modelLogo;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}