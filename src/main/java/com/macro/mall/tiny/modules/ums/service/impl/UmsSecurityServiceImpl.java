package com.macro.mall.tiny.modules.ums.service.impl;

import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.common.SecurityConstants;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminLockedMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminLoginFailureMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdminLocked;
import com.macro.mall.tiny.modules.ums.model.UmsAdminLoginFailure;
import com.macro.mall.tiny.modules.ums.service.UmsSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 安全服务实现类
 * Created by macro on 2024/01/30.
 */
@Service
public class UmsSecurityServiceImpl implements UmsSecurityService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsSecurityServiceImpl.class);
    
    @Autowired
    private UmsAdminLoginFailureMapper loginFailureMapper;
    
    @Autowired
    private UmsAdminLockedMapper lockedMapper;
    
    @Autowired
    private RedisService redisService;

    @Override
    public UmsAdminLocked checkUserLocked(String username) {
        // 先检查数据库中的锁定记录
        UmsAdminLocked lockedRecord = lockedMapper.selectByUsername(username);
        
        if (lockedRecord != null) {
            Date now = new Date();
            // 如果锁定已过期，则自动解锁
            if (lockedRecord.getUnlockTime() != null && now.after(lockedRecord.getUnlockTime())) {
                unlockUser(username);
                return null;
            }
            return lockedRecord;
        }
        
        return null;
    }

    @Override
    public boolean recordLoginFailure(String username, String ip, String reason) {
        Date now = new Date();
        
        // 创建登录失败记录
        UmsAdminLoginFailure failureRecord = new UmsAdminLoginFailure();
        failureRecord.setUsername(username);
        failureRecord.setIp(ip);
        failureRecord.setFailureTime(now);
        failureRecord.setFailureReason(reason);
        loginFailureMapper.insert(failureRecord);
        
        // 计算时间窗口
        Date windowStart = new Date(now.getTime() - TimeUnit.MINUTES.toMillis(SecurityConstants.LOGIN_FAILURE_WINDOW_MINUTES));
        
        // 统计时间窗口内的失败次数
        int failureCount = loginFailureMapper.countFailureByTimeRange(username, windowStart, now);
        
        // 如果超过阈值，则锁定用户
        if (failureCount >= SecurityConstants.MAX_LOGIN_FAILURES) {
            Date unlockTime = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(SecurityConstants.LOCK_DURATION_MINUTES));
            
            UmsAdminLocked lockedRecord = new UmsAdminLocked();
            lockedRecord.setUsername(username);
            lockedRecord.setLockTime(now);
            lockedRecord.setUnlockTime(unlockTime);
            lockedRecord.setLockReason("连续登录失败" + failureCount + "次，锁定" + SecurityConstants.LOCK_DURATION_MINUTES + "分钟");
            
            lockedMapper.insert(lockedRecord);
            
            LOGGER.warn("用户 {} 因连续登录失败被锁定，解锁时间: {}", username, unlockTime);
            return true;
        }
        
        // 清理过期的失败记录
        Date expireTime = new Date(now.getTime() - TimeUnit.MINUTES.toMillis(SecurityConstants.LOGIN_FAILURE_WINDOW_MINUTES * 2));
        loginFailureMapper.deleteFailureBeforeTime(expireTime);
        
        return false;
    }

    @Override
    public boolean unlockUser(String username) {
        int deletedCount = lockedMapper.deleteByUsername(username);
        return deletedCount > 0;
    }

    @Override
    public void recordUserToken(String username, String token) {
        // 清除之前的token
        clearUserToken(username);
        
        // 记录新的token，设置较长的过期时间（与JWT过期时间一致）
        String redisKey = SecurityConstants.REDIS_KEY_USER_TOKEN_PREFIX + username;
        redisService.set(redisKey, token, 60 * 60 * 24 * 7); // 7天
    }

    @Override
    public boolean isTokenValid(String username, String token) {
        String redisKey = SecurityConstants.REDIS_KEY_USER_TOKEN_PREFIX + username;
        String storedToken = (String) redisService.get(redisKey);
        
        // 如果没有记录的token，说明是首次登录或者已过期
        if (storedToken == null) {
            return true;
        }
        
        // 比较token是否一致
        return token.equals(storedToken);
    }

    @Override
    public void clearUserToken(String username) {
        String redisKey = SecurityConstants.REDIS_KEY_USER_TOKEN_PREFIX + username;
        redisService.del(redisKey);
    }
}