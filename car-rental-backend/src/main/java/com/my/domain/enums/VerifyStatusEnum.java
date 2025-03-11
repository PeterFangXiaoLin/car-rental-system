package com.my.domain.enums;

import lombok.Getter;

/**
 * 实名认证状态枚举
 */
@Getter
public enum VerifyStatusEnum {

    /**
     * 未实名
     */
    UNVERIFIED(0, "未实名"),

    /**
     * 认证中
     */
    VERIFYING(1, "认证中"),

    /**
     * 已认证
     */
    VERIFIED(2, "已认证"),

    /**
     * 认证失败
     */
    VERIFY_FAILED(3, "认证失败");

    /**
     * 状态码
     */
    private final int value;

    /**
     * 状态描述
     */
    private final String name;

    VerifyStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return 枚举实例
     */
    public static VerifyStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (VerifyStatusEnum status : VerifyStatusEnum.values()) {
            if (status.getValue() == code) {
                return status;
            }
        }
        return null;
    }
}