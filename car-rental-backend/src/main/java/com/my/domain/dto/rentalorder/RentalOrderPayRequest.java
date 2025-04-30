package com.my.domain.dto.rentalorder;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 租车订单支付请求
 */
@Data
public class RentalOrderPayRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;
} 