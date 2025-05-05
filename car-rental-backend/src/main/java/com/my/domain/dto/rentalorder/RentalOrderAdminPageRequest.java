package com.my.domain.dto.rentalorder;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class RentalOrderAdminPageRequest extends PageRequest implements Serializable {
    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 车辆名称
     */
    private String vehicleName;

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
     * 租赁天数
     */
    private Integer totalDays;

    /**
     * 是否需要司机：0-不需要，1-需要
     */
    private Integer needDriver;

    /**
     * 订单状态：0-待支付，1-已支付待取车，2-已取车，3-已还车，4-已完成，5-已取消
     */
    private Integer status;

    /**
     * 支付状态：0-未支付，1-已支付，2-已退款，3-部分退款
     */
    private Integer paymentStatus;

    /**
     * 取车地点
     */
    private Long pickupStoreId;

    /**
     * 还车地点
     */
    private Long returnStoreId;

}
