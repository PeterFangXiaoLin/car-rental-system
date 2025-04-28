package com.my.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.database:0}")
    private int database;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s", host, port);
        
        if (password != null && !password.isEmpty()) {
            config.useSingleServer()
                .setAddress(redisUrl)
                .setPassword(password)
                .setDatabase(database);
        } else {
            config.useSingleServer()
                .setAddress(redisUrl)
                .setDatabase(database);
        }
        
        return Redisson.create(config);
    }
}
