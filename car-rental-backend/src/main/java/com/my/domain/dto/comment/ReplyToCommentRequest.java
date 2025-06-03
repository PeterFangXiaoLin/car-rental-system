package com.my.domain.dto.comment;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReplyToCommentRequest implements Serializable {
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 评论id
     */
    private Long commentId;

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
