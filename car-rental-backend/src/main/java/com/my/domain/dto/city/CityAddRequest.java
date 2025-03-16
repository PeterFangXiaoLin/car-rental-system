package com.my.domain.dto.city;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CityAddRequest implements Serializable {
    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 行政区划编码
     */
    private String adcode;

    /**
     * 城市编码
     */
    private String citycode;

    /**
     * 中心点经度
     */
    private BigDecimal longitude;

    /**
     * 中心点纬度
     */
    private BigDecimal latitude;
}
