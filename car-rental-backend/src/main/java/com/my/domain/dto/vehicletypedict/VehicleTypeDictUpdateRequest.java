package com.my.domain.dto.vehicletypedict;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleTypeDictUpdateRequest implements Serializable {
    private static final long serialVersionUID = 5142297828819751245L;
    private Long id;
    private String typeName;
}
