package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 车辆型号表
 * @TableName vehicle_model
 */
@TableName(value ="vehicle_model")
@Data
public class VehicleModel implements Serializable {
    /**
     * 型号ID
     */
    @TableId
    private Long id;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 型号logo
     */
    private String modelLogo;

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