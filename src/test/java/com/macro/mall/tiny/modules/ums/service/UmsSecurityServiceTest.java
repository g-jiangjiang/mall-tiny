package com.macro.mall.tiny.modules.ums.service;

import com.macro.mall.tiny.modules.ums.model.UmsAdminLocked;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 安全服务测试类
 * Created by macro on 2024/01/30.
 */
@SpringBootTest
@ActiveProfiles("test")
public class UmsSecurityServiceTest {

    @Autowired
    private UmsSecurityService securityService;

    @Test
    public void testRecordLoginFailure() {
        // 测试记录登录失败
        String username = "testuser";
        String ip = "127.0.0.1";
        String reason = "密码错误";
        
        // 记录一次登录失败
        boolean isLocked = securityService.recordLoginFailure(username, ip, reason);
        assertFalse(isLocked, "第一次失败不应该锁定");
        
        // 检查用户是否被锁定
        UmsAdminLocked lockedRecord = securityService.checkUserLocked(username);
        assertNull(lockedRecord, "用户不应该被锁定");
        
        // 清理测试数据
        securityService.unlockUser(username);
    }

    @Test
    public void testUserTokenManagement() {
        // 测试用户token管理
        String username = "testuser";
        String token = "test-token-123";
        
        // 记录用户token
        securityService.recordUserToken(username, token);
        
        // 验证token有效性
        boolean isValid = securityService.isTokenValid(username, token);
        assertTrue(isValid, "记录的token应该是有效的");
        
        // 使用不同的token验证
        boolean isInvalid = securityService.isTokenValid(username, "different-token");
        assertFalse(isInvalid, "不同的token应该是无效的");
        
        // 清除用户token
        securityService.clearUserToken(username);
        
        // 再次验证，应该返回true（因为没有记录，允许新登录）
        boolean isValidAfterClear = securityService.isTokenValid(username, "new-token");
        assertTrue(isValidAfterClear, "清除token后应该允许新token");
    }
}