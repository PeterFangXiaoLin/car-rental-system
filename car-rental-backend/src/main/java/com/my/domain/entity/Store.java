package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 门店表
 * @TableName store
 */
@TableName(value ="store")
@Data
public class Store implements Serializable {
    /**
     * 门店ID
     */
    @TableId
    private Long id;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店地址
     */
    private String address;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 开始营业时间
     */
    private Date openTime;

    /**
     * 结束营业时间
     */
    private Date closeTime;

    /**
     * 状态：0-关闭，1-营业中
     */
    private Integer status;

    /**
     * 门店图片URL，多个用逗号分隔
     */
    private String images;

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