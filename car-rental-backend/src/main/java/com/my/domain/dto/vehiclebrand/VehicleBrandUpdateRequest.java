package com.my.domain.dto.vehiclebrand;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleBrandUpdateRequest implements Serializable {

    /**
     * 品牌id
     */
    private Long id;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌logo
     */
    private String brandLogo;

    /**
     * 品牌描述
     */
    private String description;
}
