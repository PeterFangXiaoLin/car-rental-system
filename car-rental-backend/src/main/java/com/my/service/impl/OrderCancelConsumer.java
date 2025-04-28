package com.my.service.impl;

import com.my.config.RabbitMQConfig;
import com.my.service.RentalOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 订单取消消息消费者
 */
@Component
@Slf4j
public class OrderCancelConsumer {

    @Resource
    private RentalOrderService rentalOrderService;

    /**
     * 监听订单取消队列，处理超时未支付的订单
     * @param orderId 订单ID
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_CANCEL_QUEUE)
    public void handleOrderCancel(Long orderId) {
        log.info("接收到订单取消消息，订单ID: {}", orderId);
        try {
            boolean result = rentalOrderService.cancelUnpaidOrder(orderId);
            if (result) {
                log.info("订单自动取消成功，订单ID: {}", orderId);
            } else {
                log.warn("订单自动取消失败，可能订单状态已变更，订单ID: {}", orderId);
            }
        } catch (Exception e) {
            log.error("处理订单取消消息异常: {}, 订单ID: {}", e.getMessage(), orderId, e);
        }
    }
} 