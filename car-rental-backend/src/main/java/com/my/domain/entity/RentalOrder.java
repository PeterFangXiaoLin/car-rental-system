package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

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
    @TableId(type = IdType.AUTO)
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
    private BigDecimal dailyPrice;

    /**
     * 租赁天数
     */
    private Integer totalDays;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 车辆损坏赔偿金额
     */
    private BigDecimal damageAmount;

    /**
     * 额外费用(如超时、违章等)
     */
    private BigDecimal extraFee;

    /**
     * 额外费用说明
     */
    private String extraFeeDesc;

    /**
     * 最终结算金额
     */
    private BigDecimal finalAmount;

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
    private BigDecimal driverPrice;

    /**
     * 司机服务总费用
     */
    private BigDecimal driverTotalAmount;

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
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 支付交易号
     */
    private String transactionId;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 退款交易号
     */
    private String refundTransactionId;

    /**
     * 取车地点
     */
    private String pickupLocation;

    /**
     * 还车地点
     */
    private String returnLocation;

    /**
     * 取车操作人ID
     */
    private Long pickupOperatorId;

    /**
     * 还车操作人ID
     */
    private Long returnOperatorId;

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
     * 用户评分(1-5分)
     */
    private Integer userRating;

    /**
     * 用户评价
     */
    private String userComment;

    /**
     * 评价时间
     */
    private Date commentTime;

    /**
     * 起始里程数
     */
    private Integer mileageStart;

    /**
     * 结束里程数
     */
    private Integer mileageEnd;

    /**
     * 起始油量
     */
    private String fuelLevelStart;

    /**
     * 结束油量
     */
    private String fuelLevelEnd;

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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}