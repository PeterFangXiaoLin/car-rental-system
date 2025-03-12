package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 车型字典表
 * @TableName vehicle_type_dict
 */
@TableName(value ="vehicle_type_dict")
@Data
public class VehicleTypeDict implements Serializable {
    /**
     * 车型ID
     */
    @TableId
    private Long id;

    /**
     * 车型名称
     */
    private String typeName;

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