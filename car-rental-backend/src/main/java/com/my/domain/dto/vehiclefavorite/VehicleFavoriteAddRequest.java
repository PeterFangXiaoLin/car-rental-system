package com.my.domain.dto.vehiclefavorite;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleFavoriteAddRequest implements Serializable {
    /**
     * 车辆 id
     */
    private Long vehicleId;

    private static final long serialVersionUID = 1L;
}
