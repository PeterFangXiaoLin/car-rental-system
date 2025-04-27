package com.my.domain.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论添加请求
 */
@Data
public class CommentAddRequest implements Serializable {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 车辆评分(1-5星)
     */
    private Integer vehicleRating;

    /**
     * 司机评分(1-5星)
     */
    private Integer driverRating;

    /**
     * 评论图片URL，多个以逗号分隔
     */
    private String images;

    private static final long serialVersionUID = 1L;
} 