package com.my.domain.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    UNPAID("待支付", 0),
    PAID_UNPICKED("已支付待取车", 1),
    PICKED("已取车", 2),
    RETURNED("已还车", 3),
    COMPLETED("已完成", 4),
    CANCELLED("已取消", 5);
    private final String name;
    private final Integer value;

    OrderStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static OrderStatusEnum getEnumByValue(Integer value) {
        for (OrderStatusEnum anEnum : OrderStatusEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
