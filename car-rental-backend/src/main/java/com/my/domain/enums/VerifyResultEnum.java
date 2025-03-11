package com.my.domain.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum VerifyResultEnum {
    REVIEWING("待审核", 0),
    PASS("通过", 1),
    REJECT("拒绝", 2);

    private final String text;
    private final int value;

    VerifyResultEnum(String text, int value) {
        this.text = text;  
        this.value = value;  
    }  
  
    /**  
     * 根据 value 获取枚举  
     */  
    public static VerifyResultEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;  
        }  
        for (VerifyResultEnum pictureVerifyResultEnum : VerifyResultEnum.values()) {
            if (pictureVerifyResultEnum.value == value) {
                return pictureVerifyResultEnum;
            }  
        }  
        return null;  
    }  
}
