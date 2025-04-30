package com.my.service.impl;

import com.my.service.RentalOrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.context.annotation.Lazy;
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

    // 重试延迟（毫秒）- 5秒
    private static final long RETRY_DELAY_MS = 5 * 1000;

    // 最大重试次数
    private static final int MAX_RETRY_COUNT = 3;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    @Lazy
    private RentalOrderService rentalOrderService;

    private RDelayedQueue<Long> delayedQueue;
    private RBlockingQueue<Long> blockingQueue;

    /**
     * 初始化延迟队列和消费线程
     */
    @PostConstruct
    public void init() {
        try {
            // 获取阻塞队列和延迟队列
            blockingQueue = redissonClient.getBlockingQueue(ORDER_DELAY_QUEUE);
            delayedQueue = redissonClient.getDelayedQueue(blockingQueue);

            // 启动消费线程
            Thread consumerThread = new Thread(this::consumeDelayMessage);
            consumerThread.setName("order-delay-queue-consumer");
            consumerThread.setDaemon(true);
            consumerThread.start();

            log.info("订单延迟队列初始化完成");
        } catch (Exception e) {
            log.error("订单延迟队列初始化失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 添加订单到延迟队列
     *
     * @param orderId 订单ID
     * @return 是否添加成功
     */
    public boolean addOrderToDelayQueue(Long orderId) {
        return addOrderToDelayQueue(orderId, 0);
    }

    /**
     * 添加订单到延迟队列（带重试）
     *
     * @param orderId    订单ID
     * @param retryCount 重试次数
     * @return 是否添加成功
     */
    private boolean addOrderToDelayQueue(Long orderId, int retryCount) {
        if (orderId == null || orderId <= 0) {
            log.error("无效的订单ID: {}", orderId);
            return false;
        }

        try {
            log.info("添加订单到延迟队列, 订单ID: {}, 延迟时间: {}ms", orderId, ORDER_TIMEOUT_MS);
            // 添加到延迟队列，30分钟后过期
            delayedQueue.offer(orderId, ORDER_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            return true;
        } catch (RedisException e) {
            log.error("添加订单到延迟队列异常: {}", e.getMessage(), e);
            // 不重试
            return false;
        } catch (Exception e) {
            log.error("添加订单到延迟队列异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 消费延迟队列消息
     */
    private void consumeDelayMessage() {
        log.info("订单延迟队列消费线程启动");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 从阻塞队列中取出元素，如果队列为空则阻塞等待
                Long orderId = blockingQueue.take();

                try {
                    // 处理超时订单
                    boolean result = rentalOrderService.cancelUnpaidOrder(orderId);
                    log.info("处理超时订单结果: {}, 订单ID: {}", result ? "成功" : "失败", orderId);
                } catch (Exception e) {
                    log.error("处理超时订单异常, 订单ID: {}, 错误: {}", orderId, e.getMessage(), e);
                }
            } catch (InterruptedException e) {
                log.error("订单延迟队列消费线程被中断: {}", e.getMessage());
                Thread.currentThread().interrupt();
                break;
            } catch (RedisException e) {
                log.error("Redis操作异常: {}", e.getMessage(), e);
                // 遇到Redis异常，暂停一段时间后继续
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } catch (Exception e) {
                log.error("订单延迟队列消费异常: {}", e.getMessage(), e);
                // 为了避免异常导致CPU占用过高，添加短暂延迟
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        log.warn("订单延迟队列消费线程退出");
    }
} 