package com.howaboutquestion.backend.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value, Duration duration){
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public boolean deleteValue(String key){
        return redisTemplate.delete(key);
    }
}
