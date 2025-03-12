package com.my.domain.dto.vehiclemodel;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleModelQueryRequest implements Serializable {
    private static final long serialVersionUID = 4513198317021345706L;

    private Long brandId;
}
