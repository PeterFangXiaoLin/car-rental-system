package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CityVO implements Serializable {
    private static final long serialVersionUID = 3963801960185974387L;
    /**
     * 城市ID
     */
    private Long id;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 行政区划编码
     */
    private String adcode;

    /**
     * 城市编码
     */
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
}
