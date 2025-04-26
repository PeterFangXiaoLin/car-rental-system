package com.my.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.ErrorCode;
import com.my.domain.dto.vehiclefavorite.VehicleFavoriteAddRequest;
import com.my.domain.dto.vehiclefavorite.VehicleFavoriteCancelRequest;
import com.my.domain.dto.vehiclefavorite.VehicleFavoriteQueryRequest;
import com.my.domain.entity.Vehicle;
import com.my.domain.entity.VehicleFavorite;
import com.my.domain.vo.LoginUserVO;
import com.my.domain.vo.VehicleFavoriteVO;
import com.my.exception.BusinessException;
import com.my.mapper.VehicleFavoriteMapper;
import com.my.service.UserService;
import com.my.service.VehicleFavoriteService;
import com.my.service.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author helloworld
* @description 针对表【vehicle_favorite(车辆收藏表)】的数据库操作Service实现
* @createDate 2025-04-25 21:12:09
*/
@Service
public class VehicleFavoriteServiceImpl extends ServiceImpl<VehicleFavoriteMapper, VehicleFavorite>
    implements VehicleFavoriteService{

    @Resource
    private VehicleFavoriteMapper vehicleFavoriteMapper;

    @Resource
    private VehicleService vehicleService;

    @Resource
    private UserService userService;

    @Resource
    private TransactionTemplate transactionTemplate;

    private static final String LOCK_KEY = "lock:vehicleFavorite:%s:%s";

    @Override
    public Long addVehicleFavorite(VehicleFavoriteAddRequest vehicleFavoriteAddRequest, HttpServletRequest request) {
        if (vehicleFavoriteAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验车辆是否存在
        Long vehicleId = vehicleFavoriteAddRequest.getVehicleId();
        if (vehicleId == null || vehicleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Vehicle vehicle = vehicleService.getById(vehicleId);
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
        }

        // 校验是否登录
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();

        VehicleFavorite vehicleFavorite = new VehicleFavorite();
        vehicleFavorite.setVehicleId(vehicleId);
        vehicleFavorite.setUserId(loginUserId);

        // 加锁
        String lockKey = String.format(LOCK_KEY, vehicleId, loginUserId);
        synchronized (lockKey.intern()) {
            // 再次校验，防止并发 transaction 编程式事务
            return transactionTemplate.execute(status -> {
                boolean isExist = this.lambdaQuery().eq(VehicleFavorite::getVehicleId, vehicleId)
                        .eq(VehicleFavorite::getUserId, loginUserId)
                        .exists();
                if (isExist) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "已收藏");
                }

                boolean result = this.save(vehicleFavorite);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "收藏失败");
                }
                return vehicleFavorite.getId();
            });
        }
    }

    @Override
    public Boolean cancelVehicleFavorite(VehicleFavoriteCancelRequest vehicleFavoriteCancelRequest, HttpServletRequest request) {
        if (vehicleFavoriteCancelRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long vehicleId = vehicleFavoriteCancelRequest.getVehicleId();
        if (vehicleId == null || vehicleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Vehicle vehicle = vehicleService.getById(vehicleId);
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
        }

        LoginUserVO loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();

        // 加锁
        String lockKey = String.format(LOCK_KEY, vehicleId, loginUserId);
        synchronized (lockKey.intern()) {
            // 再次校验，防止并发 transaction 编程式事务
            return transactionTemplate.execute(status -> {
                boolean isExist = this.lambdaQuery().eq(VehicleFavorite::getVehicleId, vehicleId)
                       .eq(VehicleFavorite::getUserId, loginUserId)
                       .exists();
                if (!isExist) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "未收藏");
                }
                boolean result = this.lambdaUpdate().eq(VehicleFavorite::getVehicleId, vehicleId)
                       .eq(VehicleFavorite::getUserId, loginUserId)
                       .remove();
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "取消收藏失败");
                }
                return true;
            });
        }
    }

    @Override
    public Page<VehicleFavoriteVO> pageVehicleFavorite(VehicleFavoriteQueryRequest vehicleFavoriteQueryRequest, HttpServletRequest request) {
        if (vehicleFavoriteQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String searchText = vehicleFavoriteQueryRequest.getSearchText();
        long current = vehicleFavoriteQueryRequest.getCurrent();
        long size = vehicleFavoriteQueryRequest.getPageSize();
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        Page<VehicleFavoriteVO> page = new Page<>(current, size);
        return vehicleFavoriteMapper.pageVehicleFavorite(page, loginUserId, searchText);
    }

    @Override
    public Boolean checkVehicleFavorite(Long vehicleId, HttpServletRequest request) {
        if (vehicleId == null || vehicleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Vehicle vehicle = vehicleService.getById(vehicleId);
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
        }
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        return this.lambdaQuery().eq(VehicleFavorite::getVehicleId, vehicleId)
              .eq(VehicleFavorite::getUserId, loginUserId)
              .exists();
    }
}




