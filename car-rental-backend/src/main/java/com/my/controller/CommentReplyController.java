package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.common.ResultUtils;
import com.my.domain.dto.comment.ReplyToCommentRequest;
import com.my.domain.dto.commentreply.CommentReplyAddRequest;
import com.my.domain.dto.commentreply.CommentReplyQueryRequest;
import com.my.domain.dto.commentreply.CommentReplyUpdateRequest;
import com.my.domain.vo.CommentReplyVO;
import com.my.service.CommentReplyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/comment/reply")
public class CommentReplyController {
    
    @Resource
    private CommentReplyService commentReplyService;

    @PostMapping("/add")
    @ApiOperation(value = "增加评论回复")
    public BaseResponse<Long> addCommentReply(@RequestBody CommentReplyAddRequest commentReplyAddRequest, HttpServletRequest request) {
        return ResultUtils.success(commentReplyService.addCommentReply(commentReplyAddRequest, request));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除评论回复")
    public BaseResponse<Boolean> deleteCommentReply(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        return ResultUtils.success(commentReplyService.deleteCommentReply(deleteRequest, request));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改评论回复")
    public BaseResponse<Boolean> updateCommentReply(@RequestBody CommentReplyUpdateRequest commentReplyUpdateRequest, HttpServletRequest request) {
        return ResultUtils.success(commentReplyService.updateCommentReply(commentReplyUpdateRequest, request));
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取评论回复详情")
    public BaseResponse<CommentReplyVO> getCommentReplyById(@RequestParam("id") Long id) {
        return ResultUtils.success(commentReplyService.getCommentReplyById(id));
    }

    @PostMapping("/list/page")
    @ApiOperation(value = "分页获取评论回复列表")
    public BaseResponse<Page<CommentReplyVO>> listCommentReplyByPage(@RequestBody CommentReplyQueryRequest commentReplyQueryRequest) {
        return ResultUtils.success(commentReplyService.listCommentReplyByPage(commentReplyQueryRequest));
    }

    @GetMapping("/list/comment")
    @ApiOperation(value = "获取评论的回复列表")
    public BaseResponse<List<CommentReplyVO>> listCommentReplyByCommentId(@RequestParam("commentId") Long commentId) {
        return ResultUtils.success(commentReplyService.listCommentReplyByCommentId(commentId));
    }

    @PostMapping("/add/reply")
    @ApiOperation(value = "追加评论")
    public BaseResponse<Boolean> addReplyToComment(@RequestBody ReplyToCommentRequest replyToCommentRequest, HttpServletRequest request) {
        return ResultUtils.success(commentReplyService.addReplyToComment(replyToCommentRequest, request));
    }
}
