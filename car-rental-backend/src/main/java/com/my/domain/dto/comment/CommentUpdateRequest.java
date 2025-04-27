package com.my.domain.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论更新请求
 */
@Data
public class CommentUpdateRequest implements Serializable {
    /**
     * 评论ID
     */
    private Long id;
    
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