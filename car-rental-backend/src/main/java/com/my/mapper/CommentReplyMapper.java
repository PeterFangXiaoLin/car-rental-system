package com.my.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.domain.dto.commentreply.CommentReplyQueryRequest;
import com.my.domain.entity.CommentReply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.domain.vo.CommentReplyVO;

import java.util.List;

/**
* @author helloworld
* @description 针对表【comment_reply(评论回复表)】的数据库操作Mapper
* @createDate 2025-05-02 15:54:40
* @Entity com.my.domain.entity.CommentReply
*/
public interface CommentReplyMapper extends BaseMapper<CommentReply> {
    /**
     * 分页查询评论回复VO
     * @param page 分页参数
     * @param queryRequest 查询请求
     * @return 分页结果
     */
    Page<CommentReplyVO> selectCommentReplyVOPage(Page<CommentReplyVO> page, CommentReplyQueryRequest queryRequest);

    /**
     * 根据评论ID查询评论回复VO列表
     * @param commentId 评论ID
     * @return 评论回复VO列表
     */
    List<CommentReplyVO> selectCommentReplyVOListByCommentId(Long commentId);
}




