package com.my.domain.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum UserRoleEnum {
    REVIEWING("用户", 0),
    PASS("司机", 1),
    REJECT("管理员", 2);

    private final String text;
    private final int value;

    UserRoleEnum(String text, int value) {
        this.text = text;  
        this.value = value;  
    }  
  
    /**  
     * 根据 value 获取枚举  
     */  
    public static UserRoleEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;  
        }  
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.value == value) {
                return userRoleEnum;
            }
        }  
        return null;  
    }  
}
