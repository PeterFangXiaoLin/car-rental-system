package com.my.domain.dto.vehiclemodel;

import com.my.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleModelQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 4513198317021345706L;

    private Long brandId; // 品牌id

    private String modelName; // 型号名称

}
