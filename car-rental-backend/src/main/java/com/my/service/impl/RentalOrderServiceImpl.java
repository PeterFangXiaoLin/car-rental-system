package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.config.AlipayConfigProperties;
import com.my.constant.OrderConstant;
import com.my.constant.UserConstant;
import com.my.domain.dto.rentalorder.RentalOrderAdminPageRequest;
import com.my.domain.dto.rentalorder.RentalOrderCancelRequest;
import com.my.domain.dto.rentalorder.RentalOrderCreateRequest;
import com.my.domain.dto.rentalorder.RentalOrderPageRequest;
import com.my.domain.dto.rentalorder.RentalOrderRefundRequest;
import com.my.domain.entity.Driver;
import com.my.domain.entity.RentalOrder;
import com.my.domain.entity.Store;
import com.my.domain.entity.Vehicle;
import com.my.domain.enums.DriverWorkStatusEnum;
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
import java.util.Date;
import java.util.List;

import static com.my.constant.RedisConstant.VEHICLE_LOCK_PREFIX;
import static com.my.constant.RedisConstant.DRIVER_LOCK_PREFIX;

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
    
    @Resource
    private AlipayClient alipayClient;

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
        order.setPaymentTime(new Date());

        // 如果支付成功，更新订单状态为已支付待取车
        if (status.equals(PaymentStatusEnum.PAID.getValue())) {
            order.setStatus(OrderStatusEnum.PAID_UNPICKED.getValue());
        }
        
        return this.updateById(order);
    }

    @Override
    public Page<RentalOrderVO> pageMyRentalOrder(RentalOrderPageRequest pageRequest, HttpServletRequest request) {
        if (pageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        long pageSize = pageRequest.getPageSize();
        long current = pageRequest.getCurrent();

        Page<RentalOrderVO> page = new Page<>(current, pageSize);
        return rentalOrderMapper.pageMyRentalOrder(page, pageRequest, userId);
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
            
            // 使用司机ID作为锁对象，确保对同一个司机的操作是线程安全的
            String driverLockKey = DRIVER_LOCK_PREFIX + driverId;
            synchronized (driverLockKey.intern()) {
                Driver driver = driverService.getById(driverId);
                if (driver == null) {
                    throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "司机不存在");
                }
                
                // 检查司机当前状态是否可接单
                if (!DriverWorkStatusEnum.AVAILABLE.getValue().equals(driver.getWorkStatus())) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "该司机当前不可接单");
                }
                
                // 更新司机状态为已接单
                driver.setWorkStatus(DriverWorkStatusEnum.OCCUPIED.getValue());
                boolean updateDriverResult = driverService.updateById(driver);
                if (!updateDriverResult) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "司机状态更新失败");
                }
                
                // 设置司机服务费用
                rentalOrder.setDriverDailyPrice(driver.getDailyPrice());
                rentalOrder.setDriverTotalAmount(driver.getDailyPrice().multiply(BigDecimal.valueOf(totalDays)));
                rentalOrder.setTotalAmount(rentalOrder.getTotalAmount().add(rentalOrder.getDriverTotalAmount()));
            }
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
        
        // 更新订单状态为已取消 并设置取消原因
        order.setStatus(OrderStatusEnum.CANCELLED.getValue());
        order.setCancelReason(OrderConstant.DEFAULT_CANCEL_REASON);
        order.setCancelTime(new Date());

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
            
            // 如果订单需要司机，释放司机资源
            if (order.getNeedDriver() == 1 && order.getDriverId() != null) {
                Long driverId = order.getDriverId();
                String driverLockKey = DRIVER_LOCK_PREFIX + driverId;
                synchronized (driverLockKey.intern()) {
                    Driver driver = driverService.getById(order.getDriverId());
                    if (driver != null && DriverWorkStatusEnum.OCCUPIED.getValue().equals(driver.getWorkStatus())) {
                        driver.setWorkStatus(DriverWorkStatusEnum.AVAILABLE.getValue());
                        boolean updateDriverResult = driverService.updateById(driver);
                        if (!updateDriverResult) {
                            log.error("取消订单失败，司机状态更新失败: {}", order.getDriverId());
                            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "司机状态更新失败");
                        }
                    }
                }
            }
        }
        
        return this.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelRentalOrder(RentalOrderCancelRequest rentalOrderCancelRequest, HttpServletRequest request) {
        if (rentalOrderCancelRequest == null || rentalOrderCancelRequest.getOrderId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }

        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long orderId = rentalOrderCancelRequest.getOrderId();

        // 查询订单是否存在
        RentalOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限取消该订单");
        }

        // 验证订单状态是否允许取消
        if (!OrderStatusEnum.UNPAID.getValue().equals(order.getStatus()) ||
           !PaymentStatusEnum.UNPAID.getValue().equals(order.getPaymentStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单状态不允许取消");
        }

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

                // 更新订单状态为已取消 并设置取消原因
                order.setStatus(OrderStatusEnum.CANCELLED.getValue());
                order.setCancelReason(rentalOrderCancelRequest.getCancelReason());
                order.setCancelTime(new Date());
                boolean saveResult = this.updateById(order);
                if (!saveResult) {
                    log.error("取消订单失败，订单状态更新失败: {}", orderId);
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单状态更新失败");
                }
                
                // 如果订单需要司机，释放司机资源
                if (order.getNeedDriver() == 1 && order.getDriverId() != null) {
                    Long driverId = order.getDriverId();
                    String driverLockKey = DRIVER_LOCK_PREFIX + driverId;
                    synchronized (driverLockKey.intern()) {
                        Driver driver = driverService.getById(order.getDriverId());
                        if (driver != null && DriverWorkStatusEnum.OCCUPIED.getValue().equals(driver.getWorkStatus())) {
                            driver.setWorkStatus(DriverWorkStatusEnum.AVAILABLE.getValue());
                            boolean updateDriverResult = driverService.updateById(driver);
                            if (!updateDriverResult) {
                                log.error("取消订单失败，司机状态更新失败: {}", order.getDriverId());
                                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "司机状态更新失败");
                            }
                        }
                    }
                }

                return true;
            }
        }
        log.error("取消订单失败，车辆状态更新失败: {}", vehicleId);
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "车辆状态更新失败");
    }

    @Override
    public RentalOrderVO getRentalOrder(Long orderId, HttpServletRequest request) {
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 查询订单是否存在
        RentalOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId) && !loginUser.getUserRole().equals(UserConstant.ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限查看该订单");
        }
        return getRentalOrderVO(orderId);
    }

    @Override
    public RentalOrderVO getRentalOrderVO(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }
        return rentalOrderMapper.getRentalOrderVO(orderId);
    }

    @Override
    public Page<RentalOrderVO> pageRentalOrder(RentalOrderAdminPageRequest pageRequest) {
        if (pageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long pageSize = pageRequest.getPageSize();
        long current = pageRequest.getCurrent();
        Page<RentalOrderVO> page = new Page<>(current, pageSize);
        return rentalOrderMapper.pageRentalOrder(page, pageRequest);
    }

    @Override
    public Boolean deleteRentalOrder(DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long deleteId = deleteRequest.getId();
        if (deleteId == null || deleteId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 查询订单是否存在
        RentalOrder order = this.getById(deleteId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除该订单");
        }
        // 删除订单
        boolean remove = this.removeById(deleteId);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单删除失败");
        }
        return true;
    }

    @Override
    public Boolean pickupVehicle(Long orderId, HttpServletRequest request) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 查询订单是否存在
        RentalOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限取车该订单");
        }
        // 验证订单状态是否允许取车
        if (!OrderStatusEnum.PAID_UNPICKED.getValue().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单状态不允许取车");
        }
        // 更新订单状态为已取车
        order.setStatus(OrderStatusEnum.PICKED.getValue());
        order.setActualStartTime(new Date());
        boolean update = this.updateById(order);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单状态更新失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean returnVehicle(Long orderId, HttpServletRequest request) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }
        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 查询订单是否存在
        RentalOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限还车该订单");
        }
        // 验证订单状态是否允许还车
        if (!OrderStatusEnum.PICKED.getValue().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单状态不允许还车");
        }
        // 更新订单状态为已还车
        order.setStatus(OrderStatusEnum.RETURNED.getValue());
        order.setActualReturnTime(new Date());
        boolean update = this.updateById(order);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单状态更新失败");
        }
        // 释放车辆资源
        Long vehicleId = order.getVehicleId();
        String lockKey = VEHICLE_LOCK_PREFIX + vehicleId;
        synchronized (lockKey.intern()) {
            Vehicle vehicle = vehicleService.getById(vehicleId);
            // 确保车辆状态是已租出
            if (vehicle!= null && VehicleStatusEnum.RENTED.getValue().equals(vehicle.getStatus())) {
                vehicle.setStatus(VehicleStatusEnum.AVAILABLE.getValue());
                boolean updateResult = vehicleService.updateById(vehicle);
                if (!updateResult) {
                    log.error("还车失败，车辆状态更新失败: {}", vehicleId);
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "车辆状态更新失败");
                }
            }
            
            // 如果订单需要司机，释放司机资源
            if (order.getNeedDriver() == 1 && order.getDriverId() != null) {
                Long driverId = order.getDriverId();
                String driverLockKey = DRIVER_LOCK_PREFIX + driverId;
                synchronized (driverLockKey.intern()) {
                    Driver driver = driverService.getById(order.getDriverId());
                    if (driver != null && DriverWorkStatusEnum.OCCUPIED.getValue().equals(driver.getWorkStatus())) {
                        driver.setWorkStatus(DriverWorkStatusEnum.AVAILABLE.getValue());
                        boolean updateDriverResult = driverService.updateById(driver);
                        if (!updateDriverResult) {
                            log.error("还车失败，司机状态更新失败: {}", order.getDriverId());
                            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "司机状态更新失败");
                        }
                    }
                }
            }
            
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean refundOrder(RentalOrderRefundRequest refundRequest, HttpServletRequest request) {
        if (refundRequest == null || refundRequest.getOrderId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }

        // 获取当前登录用户
        LoginUserVO loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        Long orderId = refundRequest.getOrderId();

        // 查询订单是否存在
        RentalOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        // 验证订单是否属于当前用户或是否为管理员
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());
        if (!order.getUserId().equals(userId) && !isAdmin) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限退款该订单");
        }

        // 验证订单状态是否允许退款（已支付但未取车）
        boolean canRefund = OrderStatusEnum.PAID_UNPICKED.getValue().equals(order.getStatus());
        boolean isPaid = PaymentStatusEnum.PAID.getValue().equals(order.getPaymentStatus());
        
        if (!canRefund || !isPaid) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单状态不允许退款");
        }

        // 计算退款金额
        BigDecimal refundAmount = refundRequest.getRefundAmount();
        if (refundAmount == null) {
            // 全额退款
            refundAmount = order.getTotalAmount();
        } else if (refundAmount.compareTo(BigDecimal.ZERO) <= 0 || refundAmount.compareTo(order.getTotalAmount()) > 0) {
            // 退款金额必须大于0且不能超过订单总金额
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "退款金额无效");
        }

        try {
            // 创建支付宝退款请求
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            
            // 构建业务参数
            String bizContent = "{"
                + "\"out_trade_no\":\"" + order.getOrderNo() + "\","
                + "\"refund_amount\":" + refundAmount + ","
                + "\"refund_reason\":\"" + refundRequest.getRefundReason() + "\""
                + "}";
            
            alipayRequest.setBizContent(bizContent);
            
            // 调用支付宝API执行退款
            AlipayTradeRefundResponse response = alipayClient.execute(alipayRequest);
            
            // 处理退款响应
            if (response.isSuccess()) {
                // 退款成功，更新订单状态
                order.setRefundAmount(refundAmount);
                order.setRefundTime(new Date());

                // 判断是全额退款还是部分退款
                boolean isFullRefund = refundAmount.compareTo(order.getTotalAmount()) == 0;
                
                if (isFullRefund) {
                    // 全额退款
                    order.setPaymentStatus(PaymentStatusEnum.REFUNDED.getValue());

                    // 如果车辆已经被租出，需要释放车辆资源
                    if (OrderStatusEnum.PAID_UNPICKED.getValue().equals(order.getStatus())) {
                        Long vehicleId = order.getVehicleId();
                        String lockKey = VEHICLE_LOCK_PREFIX + vehicleId;
                        synchronized (lockKey.intern()) {
                            Vehicle vehicle = vehicleService.getById(vehicleId);
                            if (vehicle != null && VehicleStatusEnum.RENTED.getValue().equals(vehicle.getStatus())) {
                                vehicle.setStatus(VehicleStatusEnum.AVAILABLE.getValue());
                                boolean updateResult = vehicleService.updateById(vehicle);
                                if (!updateResult) {
                                    log.error("退款失败，车辆状态更新失败: {}", vehicleId);
                                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "车辆状态更新失败");
                                }
                            }
                            
                            // 如果订单需要司机，释放司机资源
                            if (order.getNeedDriver() == 1 && order.getDriverId() != null) {
                                Long driverId = order.getDriverId();
                                String driverLockKey = DRIVER_LOCK_PREFIX + driverId;
                                synchronized (driverLockKey.intern()) {
                                    Driver driver = driverService.getById(order.getDriverId());
                                    if (driver != null && DriverWorkStatusEnum.OCCUPIED.getValue().equals(driver.getWorkStatus())) {
                                        driver.setWorkStatus(DriverWorkStatusEnum.AVAILABLE.getValue());
                                        boolean updateDriverResult = driverService.updateById(driver);
                                        if (!updateDriverResult) {
                                            log.error("退款失败，司机状态更新失败: {}", order.getDriverId());
                                            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "司机状态更新失败");
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // 部分退款
                    order.setPaymentStatus(PaymentStatusEnum.PARTIALLY_REFUNDED.getValue());
                }

                // 修改订单状态为已退款
                order.setStatus(OrderStatusEnum.REFUNDED.getValue());

                boolean updateResult = this.updateById(order);
                if (!updateResult) {
                    log.error("退款成功但更新订单状态失败，订单ID: {}", orderId);
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单状态更新失败");
                }
                
                log.info("订单退款成功，订单ID: {}，退款金额: {}", orderId, refundAmount);
                return true;
            } else {
                log.error("调用支付宝退款接口失败: {}", response.getMsg());
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "退款失败: " + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("调用支付宝退款接口异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "支付系统异常");
        }
    }
}




