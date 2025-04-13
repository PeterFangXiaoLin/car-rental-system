package com.my.domain.dto.energytypedict;

import lombok.Data;

import java.io.Serializable;

@Data
public class EnergyTypeDictUpdateRequest implements Serializable {

    /**
     * 能源类型ID
     */
    private Long id;

    /**
     * 能源类型名称
     */
    private String typeName;
}
