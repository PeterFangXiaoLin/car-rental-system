package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.domain.dto.comment.CommentAddRequest;
import com.my.domain.dto.comment.CommentQueryRequest;
import com.my.domain.dto.comment.CommentUpdateRequest;
import com.my.domain.entity.*;
import com.my.domain.enums.OrderStatusEnum;
import com.my.domain.vo.CommentVO;
import com.my.domain.vo.LoginUserVO;
import com.my.exception.BusinessException;
import com.my.mapper.*;
import com.my.service.CommentService;
import com.my.service.CommentReplyService;
import com.my.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author helloworld
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2025-04-27 23:01:28
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private RentalOrderMapper rentalOrderMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private VehicleMapper vehicleMapper;

    @Resource
    private DriverMapper driverMapper;

    @Resource
    private UserService userService;

    @Resource
    private CommentReplyService commentReplyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addComment(CommentAddRequest commentAddRequest, HttpServletRequest request) {
        if (commentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);

        // 校验订单是否存在
        Long orderId = commentAddRequest.getOrderId();
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单ID不能为空");
        }
        // 检查订单是否存在
        RentalOrder order = rentalOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        // 检验订单所属用户是否为当前登录用户
        if (!order.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您无权评论该订单");
        }
        // 校验订单状态是否为已还车或已完成
        if (!order.getStatus().equals(OrderStatusEnum.RETURNED.getValue()) && !order.getStatus().equals(OrderStatusEnum.COMPLETED.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单状态不允许评论");
        }

        // 校验车辆是否存在
        Long vehicleId = commentAddRequest.getVehicleId();
        if (vehicleId != null) {
            Vehicle vehicle = vehicleMapper.selectById(vehicleId);
            if (vehicle == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "车辆不存在");
            }
        }

        // 校验司机是否存在
        Long driverId = commentAddRequest.getDriverId();
        if (driverId != null) {
            Driver driver = driverMapper.selectById(driverId);
            if (driver == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "司机不存在");
            }
        }

        // 组装评论对象
        Comment comment = BeanUtil.toBean(commentAddRequest, Comment.class);
        comment.setUserId(loginUser.getId());
        comment.setCommentTime(new Date());

        // 插入数据
        boolean saveResult = this.save(comment);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论保存失败");
        }

        // 更新订单状态为完成
        order.setStatus(OrderStatusEnum.COMPLETED.getValue());
        int count = rentalOrderMapper.updateById(order);
        if (count == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单状态更新失败");
        }
        return comment.getId();
    }

    @Override
    public Boolean deleteComment(DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        
        // 获取评论
        Comment comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }
        
        // 只能删除自己的评论或管理员可删除所有
        if (!comment.getUserId().equals(loginUser.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除该评论");
        }
        
        // 删除评论
        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除评论失败");
        }
        
        return true;
    }

    @Override
    public Boolean updateComment(CommentUpdateRequest commentUpdateRequest, HttpServletRequest request) {
        if (commentUpdateRequest == null || commentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        
        // 获取评论
        Long id = commentUpdateRequest.getId();
        Comment comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }
        
        // 只能修改自己的评论
        if (!comment.getUserId().equals(loginUser.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改该评论");
        }
        
        // 更新评论
        Comment updateComment = BeanUtil.toBean(commentUpdateRequest, Comment.class);
        
        boolean result = this.updateById(updateComment);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新评论失败");
        }
        
        return true;
    }

    @Override
    public CommentVO getCommentById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Comment comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }
        
        return this.getCommentVO(comment);
    }

    @Override
    public Page<CommentVO> listCommentByPage(CommentQueryRequest commentQueryRequest) {
        if (commentQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 分页查询
        int current = commentQueryRequest.getCurrent();
        int pageSize = commentQueryRequest.getPageSize();
        if (current <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<CommentVO> commentVOPage = new Page<>(current, pageSize);
        return commentMapper.selectCommentVOPage(commentVOPage, commentQueryRequest);
    }

    @Override
    public List<CommentVO> listCommentByVehicleId(Long vehicleId) {
        if (vehicleId == null || vehicleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 查询车辆的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getVehicleId, vehicleId);
        queryWrapper.orderByDesc(Comment::getCommentTime);
        
        List<Comment> commentList = this.list(queryWrapper);
        
        // 转换为VO
        return commentList.stream()
                .map(this::getCommentVO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentVO getCommentVO(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        CommentVO commentVO = BeanUtil.toBean(comment, CommentVO.class);
        
        // 获取用户信息
        Long userId = comment.getUserId();
        if (userId != null && userId > 0) {
            User user = userMapper.selectById(userId);
            if (user != null) {
                commentVO.setUserName(user.getUserName());
                commentVO.setUserAvatar(user.getUserAvatar());
            }
        }
        
        // 获取车辆信息
        Long vehicleId = comment.getVehicleId();
        if (vehicleId != null && vehicleId > 0) {
            Vehicle vehicle = vehicleMapper.selectById(vehicleId);
            if (vehicle != null) {
                commentVO.setVehicleName(vehicle.getName());
            }
        }
        
        // 获取司机信息
        Long driverId = comment.getDriverId();
        if (driverId != null && driverId > 0) {
            Driver driver = driverMapper.selectById(driverId);
            if (driver != null) {
                commentVO.setDriverName(driver.getDriverName());
            }
        }
        
        // 获取评论回复列表
        commentVO.setReplyList(commentReplyService.listCommentReplyByCommentId(comment.getId()));
        
        return commentVO;
    }
}




