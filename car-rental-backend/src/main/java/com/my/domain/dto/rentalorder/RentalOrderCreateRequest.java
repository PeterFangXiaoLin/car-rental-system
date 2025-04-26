package com.my.domain.dto.rentalorder;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RentalOrderCreateRequest implements Serializable {


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
     * 租赁天数
     */
    private Integer totalDays;

    /**
     * 是否需要司机：0-不需要，1-需要
     */
    private Integer needDriver;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 取车地点门店
     */
    private Long pickupStoreId;

    /**
     * 还车地点门店
     */
    private Long returnStoreId;

    /**
     * 备注
     */
    private String remark;


    private static final long serialVersionUID = -7026937218886316865L;
}
