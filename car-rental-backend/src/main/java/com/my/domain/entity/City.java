package com.my.domain.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    @TableId
    private Long id;

    /**
     * 城市名称
     */
    @ExcelProperty(value = "城市名称")
    private String name;

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
     * 中心点经度
     */
    @ExcelProperty(value = "经度")
    private BigDecimal longitude;

    /**
     * 中心点纬度
     */
    @ExcelProperty(value = "纬度")
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