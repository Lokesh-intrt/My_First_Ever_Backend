package com.example.project1.config;

import com.example.project1.DTOs.UserResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisSerializationConfig {

    private <T> RedisCacheConfiguration cacheConfig(Class<T> valueType)
    {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JacksonJsonRedisSerializer<>(valueType)))
                .entryTtl(Duration.ofMinutes(10));

    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory)
    {
        Map<String, RedisCacheConfiguration> cacheMap = Map.of("user", cacheConfig(UserResponseDTO.class));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                .withInitialCacheConfigurations(cacheMap)
                .disableCreateOnMissingCache()
                .build();
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate()
    {
        return new StringRedisTemplate();
    }
}
