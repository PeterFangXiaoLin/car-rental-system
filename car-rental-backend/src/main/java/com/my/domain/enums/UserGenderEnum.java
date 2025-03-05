package com.my.domain.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum UserGenderEnum {
    MALE("男", 0),
    FEMALE("女", 1);;

    private final String text;
    private final int value;

    UserGenderEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     */
    public static UserGenderEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (UserGenderEnum userGenderEnum : UserGenderEnum.values()) {
            if (userGenderEnum.value == value) {
                return userGenderEnum;
            }
        }
        return null;
    }
}
