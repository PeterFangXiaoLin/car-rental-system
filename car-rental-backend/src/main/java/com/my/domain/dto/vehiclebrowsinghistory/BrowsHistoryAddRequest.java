package com.my.domain.dto.vehiclebrowsinghistory;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrowsHistoryAddRequest implements Serializable {

    // 车辆id
    private Long vehicleId;
}
