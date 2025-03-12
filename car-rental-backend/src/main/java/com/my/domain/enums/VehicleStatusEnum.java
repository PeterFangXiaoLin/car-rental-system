package com.my.domain.enums;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;

@Getter
public enum VehicleStatusEnum {
    AVAILABLE("可用", 0),
    RENTED("已租出", 1),
    MAINTENANCE("维修中", 2),
    SCRAPPED("报废", 3);

    private final String text;
    private final int value;

    VehicleStatusEnum(String text, int value) {
        this.text = text;  
        this.value = value;  
    }  
  
    /**  
     * 根据 value 获取枚举  
     */  
    public static VehicleStatusEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;  
        }  
        for (VehicleStatusEnum userRoleEnum : VehicleStatusEnum.values()) {
            if (userRoleEnum.value == value) {
                return userRoleEnum;
            }
        }  
        return null;  
    }
}
