package com.my.service.impl;

import com.my.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * RabbitMQ消息服务
 */
@Service
@Slf4j
public class RabbitMQService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送订单延迟取消消息
     * @param orderId 订单ID
     */
    public void sendOrderDelayMessage(Long orderId) {
        log.info("发送订单延迟取消消息，订单ID: {}", orderId);
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_DELAY_EXCHANGE,
                    RabbitMQConfig.ORDER_DELAY_ROUTING_KEY,
                    orderId
            );
            log.info("订单延迟取消消息发送成功，订单ID: {}", orderId);
        } catch (Exception e) {
            log.error("订单延迟取消消息发送异常: {}", e.getMessage(), e);
        }
    }
} 