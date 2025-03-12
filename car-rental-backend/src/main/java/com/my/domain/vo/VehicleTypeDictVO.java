package com.my.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车型字典表
 * @TableName vehicle_type_dict
 */
@Data
public class VehicleTypeDictVO implements Serializable {
    /**
     * 车型ID
     */
    private Long id;

    /**
     * 车型名称
     */
    private String typeName;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}