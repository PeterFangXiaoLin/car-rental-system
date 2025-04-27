package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.common.ResultUtils;
import com.my.domain.dto.comment.CommentAddRequest;
import com.my.domain.dto.comment.CommentQueryRequest;
import com.my.domain.dto.comment.CommentUpdateRequest;
import com.my.domain.vo.CommentVO;
import com.my.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    @ApiOperation(value = "增加评论")
    public BaseResponse<Long> addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request) {
        return ResultUtils.success(commentService.addComment(commentAddRequest, request));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除评论")
    public BaseResponse<Boolean> deleteComment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        return ResultUtils.success(commentService.deleteComment(deleteRequest, request));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改评论")
    public BaseResponse<Boolean> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest, HttpServletRequest request) {
        return ResultUtils.success(commentService.updateComment(commentUpdateRequest, request));
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取评论详情")
    public BaseResponse<CommentVO> getCommentById(@RequestParam("id") Long id) {
        return ResultUtils.success(commentService.getCommentById(id));
    }

    @PostMapping("/list/page")
    @ApiOperation(value = "分页获取评论列表")
    public BaseResponse<Page<CommentVO>> listCommentByPage(@RequestBody CommentQueryRequest commentQueryRequest) {
        return ResultUtils.success(commentService.listCommentByPage(commentQueryRequest));
    }

    @GetMapping("/list/vehicle")
    @ApiOperation(value = "获取车辆的评论列表")
    public BaseResponse<List<CommentVO>> listCommentByVehicleId(@RequestParam("vehicleId") Long vehicleId) {
        return ResultUtils.success(commentService.listCommentByVehicleId(vehicleId));
    }
}
