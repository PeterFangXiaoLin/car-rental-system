package com.my.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.domain.dto.comment.CommentAddRequest;
import com.my.domain.dto.comment.CommentQueryRequest;
import com.my.domain.dto.comment.CommentUpdateRequest;
import com.my.domain.entity.Comment;
import com.my.domain.entity.Driver;
import com.my.domain.entity.User;
import com.my.domain.entity.Vehicle;
import com.my.domain.vo.CommentVO;
import com.my.domain.vo.LoginUserVO;
import com.my.exception.BusinessException;
import com.my.mapper.CommentMapper;
import com.my.mapper.DriverMapper;
import com.my.mapper.UserMapper;
import com.my.mapper.VehicleMapper;
import com.my.service.CommentService;
import com.my.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import org.springframework.stereotype.Service;

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
    private UserMapper userMapper;

    @Resource
    private VehicleMapper vehicleMapper;

    @Resource
    private DriverMapper driverMapper;

    @Resource
    private UserService userService;

    @Override
    public Long addComment(CommentAddRequest commentAddRequest, HttpServletRequest request) {
        if (commentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 获取当前登录用户
        LoginUserVO loginUserVO = userService.getLoginUser(request);
        if (loginUserVO == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 获取用户完整信息
        User loginUser = userMapper.selectById(loginUserVO.getId());
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
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
        Comment comment = new Comment();
        BeanUtil.copyProperties(commentAddRequest, comment);
        comment.setUserId(loginUser.getId());
        comment.setCommentTime(new Date());
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setIsDelete(0);

        // 插入数据
        boolean saveResult = this.save(comment);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论保存失败");
        }
        
        return comment.getId();
    }

    @Override
    public Boolean deleteComment(DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 获取当前登录用户
        LoginUserVO loginUserVO = userService.getLoginUser(request);
        if (loginUserVO == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 获取用户完整信息
        User loginUser = userMapper.selectById(loginUserVO.getId());
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        
        // 获取评论
        Long id = deleteRequest.getId();
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
        LoginUserVO loginUserVO = userService.getLoginUser(request);
        if (loginUserVO == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 获取用户完整信息
        User loginUser = userMapper.selectById(loginUserVO.getId());
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        
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
        Comment updateComment = new Comment();
        BeanUtil.copyProperties(commentUpdateRequest, updateComment);
        updateComment.setUpdateTime(new Date());
        
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
        
        // 构建查询条件
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        
        // 按条件筛选
        Long userId = commentQueryRequest.getUserId();
        Long vehicleId = commentQueryRequest.getVehicleId();
        Long driverId = commentQueryRequest.getDriverId();
        Long orderId = commentQueryRequest.getOrderId();
        Integer minRating = commentQueryRequest.getMinRating();
        Integer maxRating = commentQueryRequest.getMaxRating();
        
        if (userId != null && userId > 0) {
            queryWrapper.eq(Comment::getUserId, userId);
        }
        
        if (vehicleId != null && vehicleId > 0) {
            queryWrapper.eq(Comment::getVehicleId, vehicleId);
        }
        
        if (driverId != null && driverId > 0) {
            queryWrapper.eq(Comment::getDriverId, driverId);
        }
        
        if (orderId != null && orderId > 0) {
            queryWrapper.eq(Comment::getOrderId, orderId);
        }
        
        if (minRating != null) {
            queryWrapper.ge(Comment::getVehicleRating, minRating).or().ge(Comment::getDriverRating, minRating);
        }
        
        if (maxRating != null) {
            queryWrapper.le(Comment::getVehicleRating, maxRating).or().le(Comment::getDriverRating, maxRating);
        }
        
        // 未删除
        queryWrapper.eq(Comment::getIsDelete, 0);
        // 按时间倒序
        queryWrapper.orderByDesc(Comment::getCommentTime);
        
        // 分页查询
        int current = commentQueryRequest.getCurrent();
        int pageSize = commentQueryRequest.getPageSize();
        Page<Comment> commentPage = this.page(new Page<>(current, pageSize), queryWrapper);
        
        // 转换为VO
        List<CommentVO> commentVOList = commentPage.getRecords().stream()
                .map(this::getCommentVO)
                .collect(Collectors.toList());
        
        Page<CommentVO> commentVOPage = new Page<>(current, pageSize, commentPage.getTotal());
        commentVOPage.setRecords(commentVOList);
        
        return commentVOPage;
    }

    @Override
    public List<CommentVO> listCommentByVehicleId(Long vehicleId) {
        if (vehicleId == null || vehicleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 查询车辆的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getVehicleId, vehicleId);
        queryWrapper.eq(Comment::getIsDelete, 0);
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
        
        CommentVO commentVO = BeanUtil.copyProperties(comment, CommentVO.class);
        
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
                commentVO.setDriverName(driver.getName());
            }
        }
        
        return commentVO;
    }
}




