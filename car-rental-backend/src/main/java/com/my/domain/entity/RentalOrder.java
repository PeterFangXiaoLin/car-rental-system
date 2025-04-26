package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 租赁订单表
 * @TableName rental_order
 */
@TableName(value ="rental_order")
@Data
public class RentalOrder implements Serializable {
    /**
     * 订单ID
     */
    @TableId
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 租赁开始时间
     */
    private Date startTime;

    /**
     * 租赁结束时间
     */
    private Date endTime;

    /**
     * 实际取车时间
     */
    private Date actualStartTime;

    /**
     * 实际归还时间
     */
    private Date actualReturnTime;

    /**
     * 日租金
     */
    private BigDecimal vehicleDailyPrice;

    /**
     * 租赁天数
     */
    private Integer totalDays;

    /**
     * 总金额
     */
    private BigDecimal vehicleTotalAmount;

    /**
     * 是否需要司机：0-不需要，1-需要
     */
    private Integer needDriver;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 司机服务费用/天
     */
    private BigDecimal driverDailyPrice;

    /**
     * 司机服务总费用
     */
    private BigDecimal driverTotalAmount;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态：0-待支付，1-已支付待取车，2-已取车，3-已还车，4-已完成，5-已取消
     */
    private Integer status;

    /**
     * 支付状态：0-未支付，1-已支付，2-已退款，3-部分退款
     */
    private Integer paymentStatus;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 取车地点
     */
    private Long pickupStoreId;

    /**
     * 还车地点
     */
    private Long returnStoreId;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 订单支付过期时间
     */
    private Date expireTime;

    /**
     * 备注
     */
    private String remark;

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