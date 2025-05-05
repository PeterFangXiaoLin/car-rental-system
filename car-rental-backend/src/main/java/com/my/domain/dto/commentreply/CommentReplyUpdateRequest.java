package com.my.domain.dto.commentreply;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论回复更新请求
 */
@Data
public class CommentReplyUpdateRequest implements Serializable {
    /**
     * 回复ID
     */
    private Long id;
    
    /**
     * 回复内容
     */
    private String content;

    /**
     * 回复图片URL，多个以逗号分隔
     */
    private String images;

    private static final long serialVersionUID = 1L;
} 