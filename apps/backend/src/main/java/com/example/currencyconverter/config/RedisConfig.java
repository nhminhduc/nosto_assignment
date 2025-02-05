package com.example.currencyconverter.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // The hostname "redis" is used when running via docker-compose
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("redis", 6379);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
        // Configure a default time-to-live (TTL) for cached entries
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10));
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .build();
    }
}
