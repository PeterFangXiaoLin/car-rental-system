package com.my.domain.enums;

import lombok.Getter;

/**
 * 司机状态枚举
 *
 */
@Getter
public enum DriverWorkStatusEnum {
    REST("休息中", 0),
    AVAILABLE("可接单", 1),
    OCCUPIED("已接单", 2);
    
    private final String name;
    private final Integer value;

    DriverWorkStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static DriverWorkStatusEnum getEnumByValue(Integer value) {
        for (DriverWorkStatusEnum anEnum : DriverWorkStatusEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
