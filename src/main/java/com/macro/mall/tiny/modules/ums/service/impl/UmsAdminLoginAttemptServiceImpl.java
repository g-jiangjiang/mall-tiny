package com.macro.mall.tiny.modules.ums.service.impl;

import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.ums.service.UmsAdminLoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UmsAdminLoginAttemptServiceImpl implements UmsAdminLoginAttemptService {

    @Autowired
    private RedisService redisService;

    @Value("${login.attempt.max:5}")
    private int maxAttempts;

    @Value("${login.attempt.lockDurationMinutes:30}")
    private int lockDurationMinutes;

    @Value("${login.attempt.windowMinutes:10}")
    private int windowMinutes;

    private static final String LOCK_KEY_PREFIX = "login:lock:";
    private static final String ATTEMPT_KEY_PREFIX = "login:attempt:";

    @Override
    public void loginSucceeded(String username) {
        String attemptKey = ATTEMPT_KEY_PREFIX + username;
        String lockKey = LOCK_KEY_PREFIX + username;
        redisService.del(attemptKey);
        redisService.del(lockKey);
    }

    @Override
    public void loginFailed(String username) {
        String attemptKey = ATTEMPT_KEY_PREFIX + username;
        String lockKey = LOCK_KEY_PREFIX + username;
        
        Object attemptsObj = redisService.get(attemptKey);
        int attempts = attemptsObj != null ? Integer.parseInt(attemptsObj.toString()) : 0;
        attempts++;
        
        redisService.set(attemptKey, attempts, TimeUnit.MINUTES.toSeconds(windowMinutes));
        
        if (attempts >= maxAttempts) {
            redisService.set(lockKey, System.currentTimeMillis(), TimeUnit.MINUTES.toSeconds(lockDurationMinutes));
            redisService.del(attemptKey);
        }
    }

    @Override
    public boolean isLocked(String username) {
        String lockKey = LOCK_KEY_PREFIX + username;
        Object lockTimeObj = redisService.get(lockKey);
        if (lockTimeObj == null) {
            return false;
        }
        long lockTime = Long.parseLong(lockTimeObj.toString());
        long lockDuration = TimeUnit.MINUTES.toMillis(lockDurationMinutes);
        return System.currentTimeMillis() - lockTime < lockDuration;
    }

    @Override
    public long getRemainingLockTime(String username) {
        String lockKey = LOCK_KEY_PREFIX + username;
        Long expire = redisService.getExpire(lockKey);
        if (expire == null || expire <= 0) {
            return 0;
        }
        return expire;
    }

    @Override
    public int getFailedAttempts(String username) {
        String attemptKey = ATTEMPT_KEY_PREFIX + username;
        Object attemptsObj = redisService.get(attemptKey);
        return attemptsObj != null ? Integer.parseInt(attemptsObj.toString()) : 0;
    }
}
