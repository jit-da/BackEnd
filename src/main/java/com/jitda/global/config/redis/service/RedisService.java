package com.jitda.global.config.redis.service;

import com.jitda.global.response.exception.ExceptionCode;
import com.jitda.global.response.exception.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return null;
        }
        return (String) values.get(key);
    }


    public void deleteValues(String key) {
        if(Boolean.FALSE.equals(redisTemplate.delete(key))){
            log.error("Redis 데이터를 삭제하지 못했습니다.");
            throw new RedisException(ExceptionCode.REDIS_DATA_DELETE_ERROR);
        }
    }

    public boolean checkHasKey(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

}
