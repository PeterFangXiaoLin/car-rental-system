package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.domain.dto.comment.ReplyToCommentRequest;
import com.my.domain.dto.commentreply.CommentReplyAddRequest;
import com.my.domain.dto.commentreply.CommentReplyQueryRequest;
import com.my.domain.dto.commentreply.CommentReplyUpdateRequest;
import com.my.domain.entity.Comment;
import com.my.domain.entity.CommentReply;
import com.my.domain.entity.RentalOrder;
import com.my.domain.entity.User;
import com.my.domain.enums.OrderStatusEnum;
import com.my.domain.vo.CommentReplyVO;
import com.my.domain.vo.LoginUserVO;
import com.my.exception.BusinessException;
import com.my.mapper.CommentMapper;
import com.my.mapper.CommentReplyMapper;
import com.my.mapper.UserMapper;
import com.my.service.CommentReplyService;
import com.my.service.RentalOrderService;
import com.my.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
* @author helloworld
* @description 针对表【comment_reply(评论回复表)】的数据库操作Service实现
* @createDate 2025-05-02 15:54:39
*/
@Service
public class CommentReplyServiceImpl extends ServiceImpl<CommentReplyMapper, CommentReply>
    implements CommentReplyService{

    @Resource
    private CommentReplyMapper commentReplyMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private RentalOrderService rentalOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addCommentReply(CommentReplyAddRequest commentReplyAddRequest, HttpServletRequest request) {
        if (commentReplyAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);

        // 校验评论是否存在
        Long commentId = commentReplyAddRequest.getCommentId();
        if (commentId == null || commentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评论ID不能为空");
        }
        
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }

        // 校验父回复是否存在
        Long parentId = commentReplyAddRequest.getParentId();
        if (parentId != null && parentId > 0) {
            CommentReply parentReply = this.getById(parentId);
            if (parentReply == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "父回复不存在");
            }
            
            // 确保父回复属于同一个评论
            if (!parentReply.getCommentId().equals(commentId)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "父回复不属于该评论");
            }
        }

        // 校验被回复用户是否存在
        Long replyToUserId = commentReplyAddRequest.getReplyToUserId();
        if (replyToUserId != null && replyToUserId > 0) {
            User replyToUser = userMapper.selectById(replyToUserId);
            if (replyToUser == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "被回复用户不存在");
            }
        }

        // 组装评论回复对象
        CommentReply commentReply = BeanUtil.toBean(commentReplyAddRequest, CommentReply.class);
        commentReply.setUserId(loginUser.getId());

        // 插入数据
        boolean saveResult = this.save(commentReply);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论回复保存失败");
        }

        return commentReply.getId();
    }

    @Override
    public Boolean deleteCommentReply(DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        
        // 获取评论回复
        CommentReply commentReply = this.getById(id);
        if (commentReply == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论回复不存在");
        }
        
        // 只能删除自己的评论回复或管理员可删除所有
        if (!commentReply.getUserId().equals(loginUser.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除该评论回复");
        }
        
        // 删除评论回复
        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除评论回复失败");
        }
        
        return true;
    }

    @Override
    public Boolean updateCommentReply(CommentReplyUpdateRequest commentReplyUpdateRequest, HttpServletRequest request) {
        if (commentReplyUpdateRequest == null || commentReplyUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        
        // 获取评论回复
        Long id = commentReplyUpdateRequest.getId();
        CommentReply commentReply = this.getById(id);
        if (commentReply == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论回复不存在");
        }
        
        // 只能修改自己的评论回复
        if (!commentReply.getUserId().equals(loginUser.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改该评论回复");
        }
        
        // 更新评论回复
        CommentReply updateCommentReply = BeanUtil.toBean(commentReplyUpdateRequest, CommentReply.class);
        updateCommentReply.setUpdateTime(new Date());
        
        boolean result = this.updateById(updateCommentReply);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新评论回复失败");
        }
        
        return true;
    }

    @Override
    public CommentReplyVO getCommentReplyById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        CommentReply commentReply = this.getById(id);
        if (commentReply == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论回复不存在");
        }
        
        return this.getCommentReplyVO(commentReply);
    }

    @Override
    public Page<CommentReplyVO> listCommentReplyByPage(CommentReplyQueryRequest commentReplyQueryRequest) {
        if (commentReplyQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 分页查询
        int current = commentReplyQueryRequest.getCurrent();
        int pageSize = commentReplyQueryRequest.getPageSize();
        if (current <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<CommentReplyVO> commentReplyVOPage = new Page<>(current, pageSize);
        return commentReplyMapper.selectCommentReplyVOPage(commentReplyVOPage, commentReplyQueryRequest);
    }

    @Override
    public List<CommentReplyVO> listCommentReplyByCommentId(Long commentId) {
        if (commentId == null || commentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 校验评论是否存在
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }
        
        // 查询评论的回复
        return commentReplyMapper.selectCommentReplyVOListByCommentId(commentId);
    }

    @Override
    public CommentReplyVO getCommentReplyVO(CommentReply commentReply) {
        if (commentReply == null) {
            return null;
        }
        
        CommentReplyVO commentReplyVO = BeanUtil.toBean(commentReply, CommentReplyVO.class);
        
        // 获取回复用户信息
        Long userId = commentReply.getUserId();
        if (userId != null && userId > 0) {
            User user = userMapper.selectById(userId);
            if (user != null) {
                commentReplyVO.setUserName(user.getUserName());
                commentReplyVO.setUserAvatar(user.getUserAvatar());
            }
        }
        
        // 获取被回复用户信息
        Long replyToUserId = commentReply.getReplyToUserId();
        if (replyToUserId != null && replyToUserId > 0) {
            User replyToUser = userMapper.selectById(replyToUserId);
            if (replyToUser != null) {
                commentReplyVO.setReplyToUserName(replyToUser.getUserName());
            }
        }
        
        return commentReplyVO;
    }

    @Override
    public Boolean addReplyToComment(ReplyToCommentRequest replyToCommentRequest, HttpServletRequest request) {
        if (replyToCommentRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);

        // 获取订单
        Long orderId = replyToCommentRequest.getOrderId();
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }
        // 校验订单是否存在
        RentalOrder order = rentalOrderService.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }
        // 校验订单状态
        if (!order.getStatus().equals(OrderStatusEnum.COMPLETED.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单状态错误");
        }
        
        // 根据订单id查询评论
        Comment comment = commentMapper.selectByOrderId(orderId);
        
        // 如果评论不存在，创建一个新的评论
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在或已被删除，无法追加评论");
        }
        
        // 组装评论回复对象
        CommentReply commentReply = new CommentReply();
        commentReply.setCommentId(comment.getId());
        commentReply.setUserId(loginUser.getId());
        commentReply.setReplyToUserId(order.getUserId());
        commentReply.setContent(replyToCommentRequest.getContent());
        commentReply.setImages(replyToCommentRequest.getImages());

        // 插入数据
        boolean result = this.save(commentReply);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论回复保存失败");
        }

        return true;
    }
}




