package com.my.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.domain.dto.vehiclebrowsinghistory.BrowsHistoryAddRequest;
import com.my.domain.dto.vehiclebrowsinghistory.BrowsHistoryQueryRequest;
import com.my.domain.entity.Vehicle;
import com.my.domain.entity.VehicleBrowsingHistory;
import com.my.domain.vo.LoginUserVO;
import com.my.domain.vo.VehicleBrowsingHistoryVO;
import com.my.exception.BusinessException;
import com.my.service.UserService;
import com.my.service.VehicleBrowsingHistoryService;
import com.my.mapper.VehicleBrowsingHistoryMapper;
import com.my.service.VehicleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
* @author Administrator
* @description 针对表【vehicle_browsing_history(车辆浏览历史表)】的数据库操作Service实现
* @createDate 2025-04-24 15:10:19
*/
@Service
public class VehicleBrowsingHistoryServiceImpl extends ServiceImpl<VehicleBrowsingHistoryMapper, VehicleBrowsingHistory>
    implements VehicleBrowsingHistoryService{

    @Resource
    private UserService userService;

    @Resource
    private VehicleService vehicleService;

    @Resource
    private VehicleBrowsingHistoryMapper vehicleBrowsingHistoryMapper;

    @Override
    public Long addBrowsHistory(BrowsHistoryAddRequest browsHistoryAddRequest, HttpServletRequest request) {
        if (browsHistoryAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验车辆是否存在
        Long vehicleId = browsHistoryAddRequest.getVehicleId();
        if (vehicleId == null || vehicleId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
        }
        Vehicle vehicle = vehicleService.getById(vehicleId);
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
        }

        // 校验是否登录
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();

        // 校验是否已经存在浏览历史
        VehicleBrowsingHistory browsingHistory = this.lambdaQuery()
                .eq(VehicleBrowsingHistory::getVehicleId, vehicleId)
                .eq(VehicleBrowsingHistory::getUserId, loginUserId)
                .last("LIMIT 1").one();

        if (browsingHistory != null) {
            browsingHistory.setBrowseTime(new Date());
            boolean updateResult = this.updateById(browsingHistory);
            if (!updateResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
            }
            return browsingHistory.getId();
        }


        // 插入浏览历史
        VehicleBrowsingHistory vehicleBrowsingHistory = new VehicleBrowsingHistory();
        vehicleBrowsingHistory.setVehicleId(vehicleId);
        vehicleBrowsingHistory.setUserId(loginUserId);
        vehicleBrowsingHistory.setBrowseTime(new Date());
        boolean saveResult = this.save(vehicleBrowsingHistory);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        return vehicleBrowsingHistory.getId();
    }

    @Override
    public Boolean deleteBrowsHistory(DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);

        // 查询浏览历史
        VehicleBrowsingHistory vehicleBrowsingHistory = vehicleBrowsingHistoryMapper.selectById(id);
        if (vehicleBrowsingHistory == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "浏览历史不存在");
        }

        // 校验是否是当前用户的浏览历史
        if (!vehicleBrowsingHistory.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除");
        }
        // 删除浏览历史
        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        return true;
    }

    @Override
    public Page<VehicleBrowsingHistoryVO> pageBrowseHistory(BrowsHistoryQueryRequest browsHistoryQueryRequest, HttpServletRequest request) {
        if (browsHistoryQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        long current = browsHistoryQueryRequest.getCurrent();
        long pageSize = browsHistoryQueryRequest.getPageSize();

        Page<VehicleBrowsingHistoryVO> page = new Page<>(current, pageSize);
        return vehicleBrowsingHistoryMapper.pageBrowseHistory(page, browsHistoryQueryRequest, userId);
    }
}




