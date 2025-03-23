package com.my.domain.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * 门店状态 0-关闭 1-营业中
 */
@Getter
public enum StoreStatusEnum {
    CLOSE("关闭", 0),
    OPEN("营业中", 1);


    private final String name;
    private final Integer value;

    StoreStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     */
    public static StoreStatusEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (StoreStatusEnum storeStatusEnum : StoreStatusEnum.values()) {
            if (storeStatusEnum.value == value) {
                return storeStatusEnum;
            }
        }
        return null;
    }
}
