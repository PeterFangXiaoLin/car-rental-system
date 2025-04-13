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