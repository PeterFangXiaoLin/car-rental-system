package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.ErrorCode;
import com.my.config.AlipayConfigProperties;
import com.my.domain.dto.rentalorder.RentalOrderCreateRequest;
import com.my.domain.dto.rentalorder.RentalOrderPageRequest;
import com.my.domain.entity.Driver;
import com.my.domain.entity.RentalOrder;
import com.my.domain.entity.Store;
import com.my.domain.entity.Vehicle;
import com.my.domain.enums.OrderStatusEnum;
import com.my.domain.enums.PaymentStatusEnum;
import com.my.domain.enums.VehicleStatusEnum;
import com.my.domain.vo.LoginUserVO;
import com.my.domain.vo.RentalOrderVO;
import com.my.exception.BusinessException;
import com.my.exception.ThrowUtils;
import com.my.mapper.RentalOrderMapper;
import com.my.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.my.constant.RedisConstant.VEHICLE_LOCK_PREFIX;

/**
* @author Administrator
* @description 针对表【rental_order(租赁订单表)】的数据库操作Service实现
* @createDate 2025-04-08 09:38:16
*/
@Service
@Slf4j
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

    @Resource
    private StoreService storeService;

    @Resource
    private AlipayConfigProperties alipayConfigProperties;
    
    @Resource
    private OrderDelayQueueService orderDelayQueueService;

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
            
            // 生成订单编号
            rentalOrder.setOrderNo(generateOrderNo());

            // 保存订单
            boolean saveResult = this.save(rentalOrder);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单创建失败");
            }
            
            // 使用Redisson的延迟队列，添加30分钟后自动取消的订单
            boolean addQueueResult = orderDelayQueueService.addOrderToDelayQueue(rentalOrder.getId());
            if (!addQueueResult) {
                log.warn("添加订单到延迟队列失败，将在系统任务中处理，订单ID: {}", rentalOrder.getId());
                // 这里我们不抛出异常导致事务回滚，因为订单已经创建成功
                // 如果延迟队列失败，可以通过定时任务扫描超时未支付订单来处理
            }
            
            return rentalOrder.getId();
        }
    }
    
    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        return "CAR" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }

    @Override
    public String getNotifyUrl() {
        return alipayConfigProperties.getNotifyUrl();
    }

    @Override
    public String getReturnUrl() {
        return alipayConfigProperties.getReturnUrl();
    }

    @Override
    public boolean updateOrderPayStatus(Long orderId, Integer status) {
        RentalOrder order = this.getById(orderId);
        if (order == null) {
            log.error("更新支付状态失败，订单不存在: {}", orderId);
            return false;
        }
        
        // 更新支付状态
        order.setPaymentStatus(status);
        
        // 如果支付成功，更新订单状态为已支付待取车
        if (status.equals(PaymentStatusEnum.PAID.getValue())) {
            order.setStatus(OrderStatusEnum.PAID_UNPICKED.getValue());
        }
        
        return this.updateById(order);
    }

    @Override
    public Page<RentalOrderVO> pageRentalOrder(RentalOrderPageRequest pageRequest, HttpServletRequest request) {
        if (pageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        String searchText = pageRequest.getSearchText();

        long pageSize = pageRequest.getPageSize();
        long current = pageRequest.getCurrent();

        Page<RentalOrderVO> page = new Page<>(current, pageSize);
        return rentalOrderMapper.pageRentalOrder(page, searchText, userId);
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
        // 设置日租金
        Integer totalDays = rentalOrder.getTotalDays();
        if (totalDays == null || totalDays <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "租赁天数必须大于0");
        }
        rentalOrder.setVehicleDailyPrice(vehicle.getDailyPrice());
        rentalOrder.setVehicleTotalAmount(vehicle.getDailyPrice().multiply(BigDecimal.valueOf(totalDays)));

        // 计算总金额
        rentalOrder.setTotalAmount(rentalOrder.getVehicleTotalAmount());

        // 如果选了司机，则校验司机是否存在
        if (rentalOrder.getNeedDriver() == 1) {
            Long driverId = rentalOrder.getDriverId();
            Driver driver = driverService.getById(driverId);
            if (driver == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "司机不存在");
            }
            // 设置司机服务费用
            rentalOrder.setDriverDailyPrice(driver.getDailyPrice());
            rentalOrder.setDriverTotalAmount(driver.getDailyPrice().multiply(BigDecimal.valueOf(totalDays)));
            rentalOrder.setTotalAmount(rentalOrder.getTotalAmount().add(rentalOrder.getDriverTotalAmount()));
        }

        // 校验门店是否存在
        Long pickupStoreId = rentalOrder.getPickupStoreId();
        Long returnStoreId = rentalOrder.getReturnStoreId();
        if (pickupStoreId == null || returnStoreId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "取车或还车门店不能为空");
        }
        List<Long> storeIds = new ArrayList<>();
        storeIds.add(pickupStoreId);
        storeIds.add(returnStoreId);
        boolean storesExist = storeService.lambdaQuery().in(Store::getId, storeIds).exists();
        if (!storesExist) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "取车或还车门店不存在");
        }
    }

    /**
     * 取消未支付订单
     * @param orderId 订单ID
     * @return 是否成功取消
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelUnpaidOrder(Long orderId) {
        RentalOrder order = this.getById(orderId);
        if (order == null) {
            log.error("取消订单失败，订单不存在: {}", orderId);
            return false;
        }
        
        // 只有未支付的订单才能被取消
        if (!OrderStatusEnum.UNPAID.getValue().equals(order.getStatus()) || 
            !PaymentStatusEnum.UNPAID.getValue().equals(order.getPaymentStatus())) {
            log.error("取消订单失败，订单状态不是未支付: {}", orderId);
            return false;
        }
        
        // 更新订单状态为已取消
        order.setStatus(OrderStatusEnum.CANCELLED.getValue());
        
        // 释放车辆资源
        Long vehicleId = order.getVehicleId();
        String lockKey = VEHICLE_LOCK_PREFIX + vehicleId;
        synchronized (lockKey.intern()) {
            Vehicle vehicle = vehicleService.getById(vehicleId);
            // 确保车辆状态是已租出
            if (vehicle != null && VehicleStatusEnum.RENTED.getValue().equals(vehicle.getStatus())) {
                vehicle.setStatus(VehicleStatusEnum.AVAILABLE.getValue());
                boolean updateResult = vehicleService.updateById(vehicle);
                if (!updateResult) {
                    log.error("取消订单失败，车辆状态更新失败: {}", vehicleId);
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "车辆状态更新失败");
                }
            }
        }
        
        return this.updateById(order);
    }
}




