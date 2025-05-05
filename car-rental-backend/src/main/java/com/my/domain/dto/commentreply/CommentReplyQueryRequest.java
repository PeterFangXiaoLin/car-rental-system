package com.my.domain.dto.commentreply;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 评论回复查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentReplyQueryRequest extends PageRequest implements Serializable {
    /**
     * 评论ID
     */
    private Long commentId;
    
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 父回复ID
     */
    private Long parentId;

    /**
     * 被回复用户ID
     */
    private Long replyToUserId;

    /**
     * 关键词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
} 