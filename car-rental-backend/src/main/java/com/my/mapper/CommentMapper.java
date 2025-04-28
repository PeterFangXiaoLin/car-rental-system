package com.my.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.domain.dto.comment.CommentQueryRequest;
import com.my.domain.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.domain.vo.CommentVO;
import org.apache.ibatis.annotations.Param;

/**
* @author helloworld
* @description 针对表【comment(评论表)】的数据库操作Mapper
* @createDate 2025-04-27 23:01:28
* @Entity com.my.domain.entity.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 分页查询评论
     * @param commentVOPage 评论分页对象
     * @param commentQueryRequest 评论查询请求对象
     * @return 评论分页对象
     */
    Page<CommentVO> selectCommentVOPage(Page<CommentVO> commentVOPage, @Param("req") CommentQueryRequest commentQueryRequest);
}




