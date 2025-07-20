package com.howaboutquestion.backend.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


/**
 * packageName    : com.howaboutquestion.backend.global.service<br>
 * fileName       : RedisService.java<br>
 * author         : cod0216<br>
 * date           : 2025-07-14<br>
 * description    : Redis 관련 요청을 수행하는 Service 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.07.13          cod0216           최초 생성<br>
 */
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 사용자의 Id를 키값으로 RefreshToken을 Redis의 value에 저장합니다.
     * @param key 사용자 Id
     * @param value RefreshToken
     * @param duration 저장할 기간
     */
    public void saveRefreshToken(String UserId, String RefreshToken, Duration duration){
        redisTemplate.opsForValue().set(UserId, RefreshToken, duration);
    }

    /**
     * 사용자의 Id로 조회를 통해 Refresh 토큰을 추출합니다.
     * @param key 사용자 Id
     * @return 해당 사용자의 RefreshToken 값
     */
    public String getRefreshToken(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 사용자의 Id에 해당하는 RefreshToken을 제거합니다.
     * @param key 사용자 Id
     * @return 제거 성공, 실패 여부 반환
     */
    public boolean deleteRefreshToken(String key){
        return redisTemplate.delete(key);
    }
}
