package com.my.domain.dto.rentalorder;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class RentalOrderPageRequest extends PageRequest implements Serializable {
    /**
     * 关键词
     */
    private String searchText;

    /**
     * 订单状态：0-待支付，1-已支付待取车，2-已取车，3-已还车，4-已完成，5-已取消
     */
    private Integer status;

}
