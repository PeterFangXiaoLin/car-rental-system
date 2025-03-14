package com.my.domain.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 城市表
 * @TableName city
 */
@TableName(value ="city")
@Data
@ExcelIgnoreUnannotated
public class City implements Serializable {
    /**
     * 城市ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 城市名称
     */
    @ExcelProperty(value = "城市名称")
    private String cityName;

    /**
     * 省份名称
     */
    @ExcelProperty(value = "省份名称")
    private String provinceName;

    /**
     * 行政区划编码
     */
    @ExcelProperty(value = "行政区划编码")
    private String adcode;

    /**
     * 城市编码
     */
    @ExcelProperty(value = "城市编码")
    private String citycode;

    /**
     * 行政级别（province/city/district）
     */
    private String level;

    /**
     * 中心点经度
     */
    private BigDecimal longitude;

    /**
     * 中心点纬度
     */
    private BigDecimal latitude;

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