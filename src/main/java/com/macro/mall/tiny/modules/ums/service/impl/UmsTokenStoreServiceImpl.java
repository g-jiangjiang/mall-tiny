package com.macro.mall.tiny.modules.ums.service.impl;

import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.ums.service.UmsTokenStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UmsTokenStoreServiceImpl implements UmsTokenStoreService {

    private static final String TOKEN_KEY_PREFIX = "token:store:";

    @Autowired
    private RedisService redisService;

    @Value("${jwt.expiration}")
    private Long tokenExpiration;

    @Override
    public void storeToken(String username, String token) {
        String key = TOKEN_KEY_PREFIX + username;
        redisService.set(key, token, tokenExpiration);
    }

    @Override
    public String getCurrentToken(String username) {
        String key = TOKEN_KEY_PREFIX + username;
        Object token = redisService.get(key);
        return token != null ? token.toString() : null;
    }

    @Override
    public boolean isValidToken(String username, String token) {
        String storedToken = getCurrentToken(username);
        return storedToken != null && storedToken.equals(token);
    }

    @Override
    public void removeToken(String username) {
        String key = TOKEN_KEY_PREFIX + username;
        redisService.del(key);
    }
}
