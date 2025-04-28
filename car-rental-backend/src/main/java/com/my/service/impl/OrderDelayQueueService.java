package com.my.service.impl;

import com.my.service.RentalOrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 订单延迟队列服务
 */
@Service
@Slf4j
public class OrderDelayQueueService {

    // 延迟队列名称
    private static final String ORDER_DELAY_QUEUE = "order:delay:queue";
    
    // 订单超时时间（毫秒）- 30分钟
    private static final long ORDER_TIMEOUT_MS = 30 * 60 * 1000;

    @Resource
    private RedissonClient redissonClient;
    
    @Resource
    private RentalOrderService rentalOrderService;
    
    private RDelayedQueue<Long> delayedQueue;
    private RBlockingQueue<Long> blockingQueue;
    
    /**
     * 初始化延迟队列和消费线程
     */
    @PostConstruct
    public void init() {
        // 获取阻塞队列和延迟队列
        blockingQueue = redissonClient.getBlockingQueue(ORDER_DELAY_QUEUE);
        delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        
        // 启动消费线程
        new Thread(this::consumeDelayMessage).start();
        
        log.info("订单延迟队列初始化完成");
    }
    
    /**
     * 添加订单到延迟队列
     * @param orderId 订单ID
     */
    public void addOrderToDelayQueue(Long orderId) {
        if (orderId == null || orderId <= 0) {
            log.error("无效的订单ID: {}", orderId);
            return;
        }
        
        try {
            log.info("添加订单到延迟队列, 订单ID: {}, 延迟时间: {}ms", orderId, ORDER_TIMEOUT_MS);
            // 添加到延迟队列，30分钟后过期
            delayedQueue.offer(orderId, ORDER_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("添加订单到延迟队列异常: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 消费延迟队列消息
     */
    private void consumeDelayMessage() {
        log.info("订单延迟队列消费线程启动");
        
        while (true) {
            try {
                // 从阻塞队列中取出元素，如果队列为空则阻塞等待
                Long orderId = blockingQueue.take();
                
                if (orderId != null && orderId > 0) {
                    log.info("订单延迟队列：接收到超时订单ID: {}", orderId);
                    
                    try {
                        // 处理超时订单
                        boolean result = rentalOrderService.cancelUnpaidOrder(orderId);
                        log.info("处理超时订单结果: {}, 订单ID: {}", result ? "成功" : "失败", orderId);
                    } catch (Exception e) {
                        log.error("处理超时订单异常, 订单ID: {}, 错误: {}", orderId, e.getMessage(), e);
                    }
                }
            } catch (InterruptedException e) {
                log.error("订单延迟队列消费线程被中断: {}", e.getMessage());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("订单延迟队列消费异常: {}", e.getMessage(), e);
            }
        }
    }
} 