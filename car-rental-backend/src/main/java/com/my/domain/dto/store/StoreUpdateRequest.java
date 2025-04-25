package com.my.domain.dto.store;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class StoreUpdateRequest implements Serializable {

    /**
     * 主键
     */
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
}
