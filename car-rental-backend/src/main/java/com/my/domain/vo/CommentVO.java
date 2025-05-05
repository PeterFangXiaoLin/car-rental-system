package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论视图对象
 */
@Data
public class CommentVO implements Serializable {
    /**
     * 评论ID
     */
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
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 车辆名称
     */
    private String vehicleName;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 司机姓名
     */
    private String driverName;

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
     * 评论回复列表
     */
    private List<CommentReplyVO> replyList;

    private static final long serialVersionUID = 1L;
} 