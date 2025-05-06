package com.my.domain.dto.rentalorder;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 租车订单退款请求
 */
@Data
public class RentalOrderRefundRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 退款金额，如果为null则全额退款
     */
    private BigDecimal refundAmount;

    /**
     * 退款原因
     */
    private String refundReason;
} 