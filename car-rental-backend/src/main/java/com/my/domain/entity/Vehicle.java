package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 车辆表
 * @TableName vehicle
 */
@TableName(value ="vehicle")
@Data
public class Vehicle implements Serializable {
    /**
     * 车辆ID
     */
    @TableId
    private Long id;

    /**
     * 车牌号
     */
    private String vehicleNo;

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
     * 颜色
     */
    private String color;

    /**
     * 生产年份
     */
    private Integer productionYear;

    /**
     * 日租金
     */
    private BigDecimal dailyPrice;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 行驶里程
     */
    private Integer mileage;

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