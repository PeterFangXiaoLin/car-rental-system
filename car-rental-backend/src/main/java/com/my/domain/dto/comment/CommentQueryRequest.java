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
     * 评分最小值
     */
    private Integer minRating;

    /**
     * 评分最大值
     */
    private Integer maxRating;

    private static final long serialVersionUID = 1L;
} 