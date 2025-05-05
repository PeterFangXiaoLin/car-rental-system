package com.my.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论回复视图对象
 */
@Data
public class CommentReplyVO implements Serializable {
    /**
     * 回复ID
     */
    private Long id;

    /**
     * 评论ID
     */
    private Long commentId;

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
     * 回复内容
     */
    private String content;

    /**
     * 父回复ID
     */
    private Long parentId;

    /**
     * 被回复用户ID
     */
    private Long replyToUserId;

    /**
     * 被回复用户名
     */
    private String replyToUserName;

    /**
     * 回复图片URL，多个以逗号分隔
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

    private static final long serialVersionUID = 1L;
} 