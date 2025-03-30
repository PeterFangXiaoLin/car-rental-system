package com.my.domain.dto.vehicletypedict;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleTypeDictQueryRequest extends PageRequest implements Serializable {
    private String typeName;
}
