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
     * 总租赁天数
     */
    private Integer totalDays;

    /**
     * 是否需要司机：0-不需要，1-需要
     */
    private Integer needDriver;

    /**
     * 司机ID（如选择需要司机）
     */
    private Long driverId;

    /**
     * 取车地点
     */
    private String pickupLocation;

    /**
     * 还车地点
     */
    private String returnLocation;

    /**
     * 备注
     */
    private String remark;


    private static final long serialVersionUID = -7026937218886316865L;
}
