package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 评论表
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * 评论ID
     */
    @TableId
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

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
     * 评论时间
     */
    private Date commentTime;

    /**
     * 评论图片URL，多个以逗号分隔
     */
    private String images;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}