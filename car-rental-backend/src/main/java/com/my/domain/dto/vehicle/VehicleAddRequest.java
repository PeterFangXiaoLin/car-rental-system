package com.my.domain.dto.vehicle;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 车辆表
 * @TableName vehicle
 */
@Data
public class VehicleAddRequest implements Serializable {
    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 车辆名称
     */
    private String name;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 型号ID
     */
    private Long modelId;

    /**
     * 车型ID
     */
    private Long vehicleTypeId;

    /**
     * 能源类型ID
     */
    private Long energyTypeId;

    /**
     * 生产年份
     */
    private Integer productionYear;

    /**
     * 日租金
     */
    private BigDecimal dailyPrice;

    /**
     * 座位数
     */
    private Integer seatCount;

    /**
     * 状态：0-可用，1-已租出，2-维修中，3-报废
     */
    private Integer status;

    /**
     * 车辆图片URL
     */
    private String imageUrl;

    /**
     * 车辆描述
     */
    private String description;

    private static final long serialVersionUID = 1L;
}