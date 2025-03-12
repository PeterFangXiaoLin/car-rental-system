package com.my.domain.dto.vehicletypedict;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleTypeDictAddRequest implements Serializable {
    private static final long serialVersionUID = 4513198317021345706L;

    private String typeName;
}
