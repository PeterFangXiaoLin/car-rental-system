package com.my.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.domain.entity.RentalOrder;
import com.my.domain.enums.OrderStatusEnum;
import com.my.domain.enums.PaymentStatusEnum;
import com.my.service.RentalOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 订单自动取消定时任务
 */
@Component
@EnableScheduling
@Slf4j
public class OrderCancellationScheduler {

    @Resource
    private RentalOrderService rentalOrderService;

    /**
     * 每分钟执行一次，检查是否有超过30分钟未支付的订单，将其自动取消
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void cancelUnpaidOrders() {
        log.info("开始执行未支付订单自动取消定时任务");
        
        // 计算30分钟前的时间
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        Date thirtyMinutesAgoDate = Date.from(thirtyMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());
        
        // 查询所有未支付且创建时间超过30分钟的订单
        LambdaQueryWrapper<RentalOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentalOrder::getStatus, OrderStatusEnum.UNPAID.getValue())
                .eq(RentalOrder::getPaymentStatus, PaymentStatusEnum.UNPAID.getValue())
                .lt(RentalOrder::getCreateTime, thirtyMinutesAgoDate);
        
        List<RentalOrder> unpaidOrders = rentalOrderService.list(queryWrapper);
        
        for (RentalOrder order : unpaidOrders) {
            try {
                boolean result = rentalOrderService.cancelUnpaidOrder(order.getId());
                if (result) {
                    log.info("成功自动取消超时未支付订单: {}", order.getOrderNo());
                } else {
                    log.error("自动取消超时未支付订单失败: {}", order.getOrderNo());
                }
            } catch (Exception e) {
                log.error("自动取消超时未支付订单异常: {}, 订单号: {}", e.getMessage(), order.getOrderNo(), e);
            }
        }
        
        log.info("未支付订单自动取消定时任务结束，共处理{}个订单", unpaidOrders.size());
    }
} 