package com.my.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentStatusEnum {

    UNPAID("未支付", 0),
    PAID("已支付", 1),
    REFUNDED("已退款", 2),
    PARTIALLY_REFUNDED("部分退款", 3);

    private final String name;
    private final Integer value;

    PaymentStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static PaymentStatusEnum getEnumByValue(Integer value) {
        for (PaymentStatusEnum anEnum : PaymentStatusEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
