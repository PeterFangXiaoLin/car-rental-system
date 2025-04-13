package com.my.domain.dto.energytypedict;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyTypeDictQueryRequest extends PageRequest implements Serializable {
    /**
     * 能源类型名称
     */
    private String typeName;
}
