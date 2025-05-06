package com.my.domain.dto.rentalorder;

import lombok.Data;

import java.io.Serializable;

@Data
public class RentalOrderCancelRequest implements Serializable {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 取消原因
     */
    private String cancelReason;
}
