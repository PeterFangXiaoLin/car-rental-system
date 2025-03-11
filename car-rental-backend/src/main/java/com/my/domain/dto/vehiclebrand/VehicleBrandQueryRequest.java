package com.my.domain.dto.vehiclebrand;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 车辆品牌查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleBrandQueryRequest extends PageRequest implements Serializable {
    private String searchText;
}
