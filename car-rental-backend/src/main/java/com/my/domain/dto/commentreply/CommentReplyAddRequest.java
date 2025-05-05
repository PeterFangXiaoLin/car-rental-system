package com.my.domain.dto.commentreply;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论回复添加请求
 */
@Data
public class CommentReplyAddRequest implements Serializable {
    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 父回复ID（回复其他回复时使用）
     */
    private Long parentId;

    /**
     * 被回复用户ID
     */
    private Long replyToUserId;

    /**
     * 回复图片URL，多个以逗号分隔
     */
    private String images;

    private static final long serialVersionUID = 1L;
} 