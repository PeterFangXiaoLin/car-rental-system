package com.my.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论回复表
 * @TableName comment_reply
 */
@TableName(value ="comment_reply")
@Data
public class CommentReply implements Serializable {
    /**
     * 回复ID
     */
    @TableId
    private Long id;

    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 回复用户ID
     */
    private Long userId;

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