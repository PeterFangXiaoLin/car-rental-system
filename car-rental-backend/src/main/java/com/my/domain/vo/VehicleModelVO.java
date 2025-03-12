package com.my.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆型号表
 * @TableName vehicle_model
 */
@Data
public class VehicleModelVO implements Serializable {
    /**
     * 型号ID
     */
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
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}