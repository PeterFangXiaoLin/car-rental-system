package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 能源类型字典表
 * @TableName energy_type_dict
 */
@Data
public class EnergyTypeDictVO implements Serializable {
    /**
     * 能源类型ID
     */
    private Long id;

    /**
     * 能源类型名称
     */
    private String typeName;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}