package com.example.project1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Duration;

@Service
@Slf4j
public class RedisJwtBlackListing {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisJwtBlackListing(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void blacklistJwt(String jti, Date expireTime) {
        long remainingMillis = expireTime.getTime() - System.currentTimeMillis();
        if (remainingMillis > 0) {
            String key = "jwt:blacklist:" + jti;
            stringRedisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofMillis(remainingMillis));
        }
    }


    public boolean checkRedis(String jti) {

        return stringRedisTemplate.opsForValue().get("jwt:blacklist:" + jti) != null;
    }
}
