package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆浏览历史表
 *
 * @TableName vehicle_browsing_history
 */
@Data
public class VehicleBrowsingHistoryVO implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 车辆名称
     */
    private String name;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 型号ID
     */
    private Long modelId;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 车型ID
     */
    private Long vehicleTypeId;

    /**
     * 车型名称
     */
    private String vehicleTypeName;

    /**
     * 能源类型ID
     */
    private Long energyTypeId;

    /**
     * 能源类型名称
     */
    private String energyTypeName;

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

    /**
     * 浏览时间
     */
    private Date browseTime;

    private static final long serialVersionUID = 1L;
}