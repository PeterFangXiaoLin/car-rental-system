package com.my.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置类
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // 订单队列常量
    public static final String ORDER_DELAY_QUEUE = "order.delay.queue";
    public static final String ORDER_DELAY_EXCHANGE = "order.delay.exchange";
    public static final String ORDER_DELAY_ROUTING_KEY = "order.delay.routingKey";
    
    public static final String ORDER_CANCEL_QUEUE = "order.cancel.queue";
    public static final String ORDER_CANCEL_EXCHANGE = "order.cancel.exchange";
    public static final String ORDER_CANCEL_ROUTING_KEY = "order.cancel.routingKey";
    
    // 延迟时间 - 30分钟 (30 * 60 * 1000 毫秒)
    public static final int ORDER_DELAY_TIME = 30 * 60 * 1000;

    /**
     * 消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    /**
     * 延迟队列配置
     * 延迟队列设置了TTL和DLX，当消息过期时会自动路由到取消订单队列
     */
    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> args = new HashMap<>(3);
        // 设置死信交换机
        args.put("x-dead-letter-exchange", ORDER_CANCEL_EXCHANGE);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", ORDER_CANCEL_ROUTING_KEY);
        // 设置消息过期时间 30分钟
        args.put("x-message-ttl", ORDER_DELAY_TIME);
        
        return QueueBuilder.durable(ORDER_DELAY_QUEUE)
                .withArguments(args)
                .build();
    }

    /**
     * 延迟交换机
     */
    @Bean
    public DirectExchange orderDelayExchange() {
        return new DirectExchange(ORDER_DELAY_EXCHANGE);
    }

    /**
     * 将延迟队列绑定到延迟交换机
     */
    @Bean
    public Binding bindingOrderDelayQueue() {
        return BindingBuilder.bind(orderDelayQueue())
                .to(orderDelayExchange())
                .with(ORDER_DELAY_ROUTING_KEY);
    }

    /**
     * 订单取消队列
     */
    @Bean
    public Queue orderCancelQueue() {
        return new Queue(ORDER_CANCEL_QUEUE, true);
    }

    /**
     * 订单取消交换机
     */
    @Bean
    public DirectExchange orderCancelExchange() {
        return new DirectExchange(ORDER_CANCEL_EXCHANGE);
    }

    /**
     * 将取消订单队列绑定到取消订单交换机
     */
    @Bean
    public Binding bindingOrderCancelQueue() {
        return BindingBuilder.bind(orderCancelQueue())
                .to(orderCancelExchange())
                .with(ORDER_CANCEL_ROUTING_KEY);
    }
} 