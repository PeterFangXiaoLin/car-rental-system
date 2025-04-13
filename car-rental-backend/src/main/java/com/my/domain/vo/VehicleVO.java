package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class VehicleVO implements Serializable {
    /**
     * 车辆ID
     */
    private Long id;

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
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = -1835281349822252206L;
}
