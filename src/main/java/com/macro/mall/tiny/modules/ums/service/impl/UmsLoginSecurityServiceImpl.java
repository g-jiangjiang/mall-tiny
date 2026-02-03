package com.macro.mall.tiny.modules.ums.service.impl;

import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.config.LoginSecurityConfig;
import com.macro.mall.tiny.modules.ums.service.UmsLoginSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 登录安全Service实现类
 * Created by macro on 2024/01/01.
 */
@Service
public class UmsLoginSecurityServiceImpl implements UmsLoginSecurityService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private LoginSecurityConfig loginSecurityConfig;

    @Value("${redis.database}")
    private String redisDatabase;

    @Value("${redis.key.loginFail}")
    private String loginFailKey;

    @Value("${redis.key.loginLock}")
    private String loginLockKey;

    @Value("${redis.key.userToken}")
    private String userTokenKey;

    @Value("${redis.expire.loginFail}")
    private Long loginFailExpire;

    @Value("${redis.expire.loginLock}")
    private Long loginLockExpire;

    @Value("${jwt.expiration}")
    private Long tokenExpiration;

    @Override
    public boolean isAccountLocked(String username) {
        String lockKey = buildLockKey(username);
        return redisService.hasKey(lockKey);
    }

    @Override
    public void recordLoginFail(String username) {
        String failKey = buildFailKey(username);
        String lockKey = buildLockKey(username);

        // 增加失败次数
        Long failCount = redisService.incr(failKey, 1);

        // 设置失败计数过期时间（只在第一次设置）
        if (failCount != null && failCount == 1) {
            redisService.expire(failKey, loginFailExpire);
        }

        // 检查是否达到最大失败次数
        if (failCount != null && failCount >= loginSecurityConfig.getMaxFailAttempts()) {
            // 锁定账号
            redisService.set(lockKey, "locked", loginLockExpire);
            // 清除失败计数
            redisService.del(failKey);
        }
    }

    @Override
    public void clearLoginFail(String username) {
        String failKey = buildFailKey(username);
        redisService.del(failKey);
    }

    @Override
    public long getRemainingLockTime(String username) {
        String lockKey = buildLockKey(username);
        Long expire = redisService.getExpire(lockKey);
        return expire != null ? expire : 0;
    }

    @Override
    public void storeUserToken(String username, String token) {
        String tokenKey = buildTokenKey(username);
        // Token有效期与JWT一致
        redisService.set(tokenKey, token, tokenExpiration);
    }

    @Override
    public boolean validateUserToken(String username, String token) {
        String tokenKey = buildTokenKey(username);
        Object storedToken = redisService.get(tokenKey);
        if (storedToken == null) {
            return false;
        }
        return storedToken.equals(token);
    }

    @Override
    public void removeUserToken(String username) {
        String tokenKey = buildTokenKey(username);
        redisService.del(tokenKey);
    }

    private String buildFailKey(String username) {
        return redisDatabase + ":" + loginFailKey + ":" + username;
    }

    private String buildLockKey(String username) {
        return redisDatabase + ":" + loginLockKey + ":" + username;
    }

    private String buildTokenKey(String username) {
        return redisDatabase + ":" + userTokenKey + ":" + username;
    }
}
