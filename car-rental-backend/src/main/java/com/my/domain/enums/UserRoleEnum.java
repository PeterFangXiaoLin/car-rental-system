package com.my.domain.enums;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;

@Getter
public enum UserRoleEnum {
    USER("用户", 0),
    DRIVER("司机", 1),
    ADMIN("管理员", 2);

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

    /**
     * 根据 text 获取枚举
     *
     * @param text
     * @return
     */
    public static UserRoleEnum getEnumByName(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.text.equals(text)) {
                return userRoleEnum;
            }
        }
        return null;
    }
}
