package com.my.domain.dto.vehiclebrand;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleBrandAddRequest implements Serializable {
    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌logo
     */
    private String brandLogo;

    /**
     * 首字母
     */
    private String firstLetter;
}
