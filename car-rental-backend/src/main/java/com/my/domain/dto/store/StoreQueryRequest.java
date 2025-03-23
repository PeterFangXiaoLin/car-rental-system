package com.my.domain.dto.store;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreQueryRequest extends PageRequest implements Serializable {
    /**
     * id
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
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 行政区划编码（高德地图）
     */
    private String adcode;

    /**
     * 城市编码（高德地图）
     */
    private String citycode;

    /**
     * 联系电话
     */
    private String contactPhone;

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
     * 门店描述
     */
    private String description;
}
