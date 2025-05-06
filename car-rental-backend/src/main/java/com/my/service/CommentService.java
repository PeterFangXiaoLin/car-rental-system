package com.my.service;

import com.my.domain.dto.comment.CommentQueryRequest;
import com.my.domain.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.DeleteRequest;
import com.my.domain.dto.comment.CommentAddRequest;
import com.my.domain.dto.comment.CommentAdminQueryRequest;
import com.my.domain.dto.comment.CommentUpdateRequest;
import com.my.domain.vo.CommentVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author helloworld
* @description 针对表【comment(评论表)】的数据库操作Service
* @createDate 2025-04-27 23:01:28
*/
public interface CommentService extends IService<Comment> {
    
    /**
     * 添加评论
     * @param commentAddRequest 评论添加请求
     * @param request HTTP请求
     * @return 评论ID
     */
    Long addComment(CommentAddRequest commentAddRequest, HttpServletRequest request);

    /**
     * 删除评论
     * @param deleteRequest 评论删除请求
     * @param request HTTP请求
     * @return 是否成功
     */
    Boolean deleteComment(DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 更新评论
     * @param commentUpdateRequest 评论更新请求
     * @param request HTTP请求
     * @return 是否成功
     */
    Boolean updateComment(CommentUpdateRequest commentUpdateRequest, HttpServletRequest request);

    /**
     * 获取评论详情
     * @param id 评论ID
     * @return 评论视图对象
     */
    CommentVO getCommentById(Long id);

    /**
     * 分页获取评论列表
     * @param commentAdminQueryRequest 评论查询请求
     * @return 评论分页列表
     */
    Page<CommentVO> listCommentByPage(CommentAdminQueryRequest commentAdminQueryRequest);

    /**
     * 获取车辆的评论列表
     * @param vehicleId 车辆ID
     * @return 评论列表
     */
    List<CommentVO> listCommentByVehicleId(Long vehicleId);

    /**
     * 获取评论视图对象
     * @param comment 评论实体
     * @return 评论视图对象
     */
    CommentVO getCommentVO(Comment comment);

    Page<CommentVO> pageCommentByVehicleId(CommentQueryRequest commentQueryRequest);
}
