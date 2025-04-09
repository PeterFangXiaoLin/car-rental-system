package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.ErrorCode;
import com.my.domain.dto.rentalorder.RentalOrderCreateRequest;
import com.my.domain.entity.Driver;
import com.my.domain.entity.RentalOrder;
import com.my.domain.entity.Vehicle;
import com.my.domain.enums.OrderStatusEnum;
import com.my.domain.enums.PaymentStatusEnum;
import com.my.domain.enums.VehicleStatusEnum;
import com.my.domain.vo.LoginUserVO;
import com.my.exception.BusinessException;
import com.my.exception.ThrowUtils;
import com.my.service.DriverService;
import com.my.service.RentalOrderService;
import com.my.mapper.RentalOrderMapper;
import com.my.service.UserService;
import com.my.service.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.my.constant.RedisConstant.VEHICLE_LOCK_PREFIX;

/**
* @author Administrator
* @description 针对表【rental_order(租赁订单表)】的数据库操作Service实现
* @createDate 2025-04-08 09:38:16
*/
@Service
public class RentalOrderServiceImpl extends ServiceImpl<RentalOrderMapper, RentalOrder>
    implements RentalOrderService{

    @Resource
    private RentalOrderMapper rentalOrderMapper;

    @Resource
    private UserService userService;

    @Resource
    private VehicleService vehicleService;

    @Resource
    private DriverService driverService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRentalOrder(RentalOrderCreateRequest rentalOrderCreateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(rentalOrderCreateRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        RentalOrder rentalOrder = BeanUtil.toBean(rentalOrderCreateRequest, RentalOrder.class);
        rentalOrder.setUserId(loginUser.getId());

        validate(rentalOrder, true);

        // 更新车辆状态为已租出
        // 可能存在同时更新同一辆车的情况，需要加锁或使用分布式锁来保证并发安全
        Long vehicleId = rentalOrder.getVehicleId();
        String lockKey = VEHICLE_LOCK_PREFIX + vehicleId;
        synchronized (lockKey.intern()) {
            Vehicle vehicle = vehicleService.getById(vehicleId);
            vehicle.setStatus(VehicleStatusEnum.RENTED.getValue());
            boolean updateResult = vehicleService.updateById(vehicle);
            if (!updateResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "车辆状态更新失败");
            }
            // 设置订单状态为待支付
            rentalOrder.setStatus(OrderStatusEnum.UNPAID.getValue());
            // 设置支付状态为未支付
            rentalOrder.setPaymentStatus(PaymentStatusEnum.UNPAID.getValue());

            // 保存订单
            boolean saveResult = this.save(rentalOrder);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单创建失败");
            }
            return rentalOrder.getId();
        }
    }


    private void validate(RentalOrder rentalOrder, boolean isAdd) {
        if (rentalOrder == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验车辆是否存在
        Long vehicleId = rentalOrder.getVehicleId();
        Vehicle vehicle = vehicleService.getById(vehicleId);
        if (vehicle == null || !vehicle.getStatus().equals(VehicleStatusEnum.AVAILABLE.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "车辆不存在或不可用");
        }

        // 如果选了司机，则校验司机是否存在
        if (rentalOrder.getNeedDriver() == 1) {
            Long driverId = rentalOrder.getDriverId();
            Driver driver = driverService.getById(driverId);
            if (driver == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "司机不存在");
            }
        }
    }
}




