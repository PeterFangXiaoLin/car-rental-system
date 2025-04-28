package com.my.domain.dto.comment;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 评论查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 关键词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
} 