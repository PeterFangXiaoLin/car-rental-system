package com.my.domain.dto.vehicle;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 搜索内容
     */
    private String searchText;

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
     * 最小日租金
     */
    private BigDecimal minDailyPrice;

    /**
     * 最大日租金
     */
    private BigDecimal maxDailyPrice;

    /**
     * 座位数
     */
    private Integer seatCount;

    /**
     * 状态：0-可用，1-已租出，2-维修中，3-报废
     */
    private Integer status;

    /**
     * 车辆描述
     */
    private String description;

    private static final long serialVersionUID = 409012383860543059L;
}
