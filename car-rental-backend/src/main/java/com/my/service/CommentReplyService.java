package com.my.service;

import com.my.domain.entity.CommentReply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.DeleteRequest;
import com.my.domain.dto.commentreply.CommentReplyAddRequest;
import com.my.domain.dto.commentreply.CommentReplyQueryRequest;
import com.my.domain.dto.commentreply.CommentReplyUpdateRequest;
import com.my.domain.vo.CommentReplyVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author helloworld
* @description 针对表【comment_reply(评论回复表)】的数据库操作Service
* @createDate 2025-05-02 15:54:40
*/
public interface CommentReplyService extends IService<CommentReply> {
    
    /**
     * 添加评论回复
     * @param commentReplyAddRequest 评论回复添加请求
     * @param request HTTP请求
     * @return 评论回复ID
     */
    Long addCommentReply(CommentReplyAddRequest commentReplyAddRequest, HttpServletRequest request);

    /**
     * 删除评论回复
     * @param deleteRequest 删除请求
     * @param request HTTP请求
     * @return 是否成功
     */
    Boolean deleteCommentReply(DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 更新评论回复
     * @param commentReplyUpdateRequest 评论回复更新请求
     * @param request HTTP请求
     * @return 是否成功
     */
    Boolean updateCommentReply(CommentReplyUpdateRequest commentReplyUpdateRequest, HttpServletRequest request);

    /**
     * 获取评论回复详情
     * @param id 评论回复ID
     * @return 评论回复视图对象
     */
    CommentReplyVO getCommentReplyById(Long id);

    /**
     * 分页获取评论回复列表
     * @param commentReplyQueryRequest 评论回复查询请求
     * @return 评论回复分页列表
     */
    Page<CommentReplyVO> listCommentReplyByPage(CommentReplyQueryRequest commentReplyQueryRequest);

    /**
     * 获取评论的回复列表
     * @param commentId 评论ID
     * @return 评论回复列表
     */
    List<CommentReplyVO> listCommentReplyByCommentId(Long commentId);

    /**
     * 获取评论回复视图对象
     * @param commentReply 评论回复实体
     * @return 评论回复视图对象
     */
    CommentReplyVO getCommentReplyVO(CommentReply commentReply);
}
