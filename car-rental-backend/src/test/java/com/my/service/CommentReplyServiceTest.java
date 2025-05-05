package com.my.service;

import com.my.common.DeleteRequest;
import com.my.domain.dto.commentreply.CommentReplyAddRequest;
import com.my.domain.dto.commentreply.CommentReplyQueryRequest;
import com.my.domain.dto.commentreply.CommentReplyUpdateRequest;
import com.my.domain.vo.CommentReplyVO;
import com.my.domain.vo.LoginUserVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentReplyServiceTest {
    
    @Resource
    private CommentReplyService commentReplyService;
    
    @Resource
    private UserService userService;
    
    /**
     * 测试添加评论回复
     */
    @Test
    @Transactional
    void testAddCommentReply() {
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(1L);
        when(userService.getLoginUser(request)).thenReturn(loginUserVO);
        
        // 创建评论回复请求
        CommentReplyAddRequest addRequest = new CommentReplyAddRequest();
        addRequest.setCommentId(1L); // 假设存在ID为1的评论
        addRequest.setContent("这是一条测试回复");
        
        // 执行添加操作
        Long replyId = commentReplyService.addCommentReply(addRequest, request);
        
        // 验证添加结果
        assertNotNull(replyId);
        assertTrue(replyId > 0);
        
        // 验证添加的评论回复信息
        CommentReplyVO commentReplyVO = commentReplyService.getCommentReplyById(replyId);
        assertEquals(addRequest.getCommentId(), commentReplyVO.getCommentId());
        assertEquals(addRequest.getContent(), commentReplyVO.getContent());
        assertEquals(loginUserVO.getId(), commentReplyVO.getUserId());
    }
    
    /**
     * 测试更新评论回复
     */
    @Test
    @Transactional
    void testUpdateCommentReply() {
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(1L);
        when(userService.getLoginUser(request)).thenReturn(loginUserVO);
        
        // 先添加一条评论回复
        CommentReplyAddRequest addRequest = new CommentReplyAddRequest();
        addRequest.setCommentId(1L);
        addRequest.setContent("原始回复内容");
        Long replyId = commentReplyService.addCommentReply(addRequest, request);
        
        // 创建更新请求
        CommentReplyUpdateRequest updateRequest = new CommentReplyUpdateRequest();
        updateRequest.setId(replyId);
        updateRequest.setContent("更新后的回复内容");
        
        // 执行更新操作
        boolean updateResult = commentReplyService.updateCommentReply(updateRequest, request);
        
        // 验证更新结果
        assertTrue(updateResult);
        
        // 验证更新的评论回复信息
        CommentReplyVO updatedReply = commentReplyService.getCommentReplyById(replyId);
        assertEquals(updateRequest.getContent(), updatedReply.getContent());
    }
    
    /**
     * 测试删除评论回复
     */
    @Test
    @Transactional
    void testDeleteCommentReply() {
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(1L);
        when(userService.getLoginUser(request)).thenReturn(loginUserVO);
        
        // 先添加一条评论回复
        CommentReplyAddRequest addRequest = new CommentReplyAddRequest();
        addRequest.setCommentId(1L);
        addRequest.setContent("将被删除的回复");
        Long replyId = commentReplyService.addCommentReply(addRequest, request);
        
        // 创建删除请求
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setId(replyId);
        
        // 执行删除操作
        boolean deleteResult = commentReplyService.deleteCommentReply(deleteRequest, request);
        
        // 验证删除结果
        assertTrue(deleteResult);
    }
    
    /**
     * 测试查询评论的回复列表
     */
    @Test
    void testListCommentReplyByCommentId() {
        // 假设评论ID为1的评论存在
        Long commentId = 1L;
        
        // 查询该评论的回复列表
        List<CommentReplyVO> replyList = commentReplyService.listCommentReplyByCommentId(commentId);
        
        // 验证查询结果不为空
        assertNotNull(replyList);
    }
} 