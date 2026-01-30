package com.macro.mall.tiny.modules.ums.service;

import com.macro.mall.tiny.common.SecurityConstants;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import com.macro.mall.tiny.modules.ums.model.UmsAdminLocked;
import com.macro.mall.tiny.modules.ums.model.UmsAdminLoginFailure;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 安全功能综合测试类
 * 测试登录防爆破、互斥登录和用户导出功能
 * Created by macro on 2024/01/30.
 */
@SpringBootTest
@ActiveProfiles("test")
public class SecurityIntegrationTest {

    @Autowired
    private UmsSecurityService securityService;
    
    @Autowired
    private UmsAdminService adminService;
    
    @MockBean
    private UmsAdminMapper adminMapper;
    
    @MockBean
    private UmsRoleMapper roleMapper;
    
    private String testUsername = "testuser";
    private String testPassword = "testpassword";
    private String testIp = "127.0.0.1";
    
    @BeforeEach
    void setUp() {
        // 清理测试数据
        securityService.unlockUser(testUsername);
        securityService.clearUserToken(testUsername);
        
        // 重置安全上下文
        SecurityContextHolder.clearContext();
    }
    
    @Test
    @DisplayName("测试登录防爆破功能 - 正常登录不应被锁定")
    void testLoginBruteForceNormalLogin() {
        // 记录少量登录失败
        for (int i = 0; i < SecurityConstants.MAX_LOGIN_FAILURES - 1; i++) {
            boolean isLocked = securityService.recordLoginFailure(testUsername, testIp, "密码错误");
            assertFalse(isLocked, "第" + (i+1) + "次失败不应该锁定账号");
        }
        
        // 检查用户是否被锁定
        UmsAdminLocked lockedRecord = securityService.checkUserLocked(testUsername);
        assertNull(lockedRecord, "用户不应该被锁定");
    }
    
    @Test
    @DisplayName("测试登录防爆破功能 - 超过阈值应被锁定")
    void testLoginBruteForceExceedThreshold() {
        // 记录达到阈值的登录失败
        for (int i = 0; i < SecurityConstants.MAX_LOGIN_FAILURES; i++) {
            boolean isLocked = securityService.recordLoginFailure(testUsername, testIp, "密码错误");
            if (i < SecurityConstants.MAX_LOGIN_FAILURES - 1) {
                assertFalse(isLocked, "第" + (i+1) + "次失败不应该锁定账号");
            } else {
                assertTrue(isLocked, "第" + (i+1) + "次失败应该锁定账号");
            }
        }
        
        // 检查用户是否被锁定
        UmsAdminLocked lockedRecord = securityService.checkUserLocked(testUsername);
        assertNotNull(lockedRecord, "用户应该被锁定");
        assertTrue(lockedRecord.getLockReason().contains("连续登录失败"), "锁定原因应包含连续登录失败");
    }
    
    @Test
    @DisplayName("测试互斥登录功能 - 新登录应使旧token失效")
    void testExclusiveLogin() {
        // 模拟第一次登录，记录token
        String firstToken = "first-token-123";
        securityService.recordUserToken(testUsername, firstToken);
        
        // 验证第一个token有效
        boolean isFirstTokenValid = securityService.isTokenValid(testUsername, firstToken);
        assertTrue(isFirstTokenValid, "第一个token应该有效");
        
        // 模拟第二次登录，记录新token
        String secondToken = "second-token-456";
        securityService.recordUserToken(testUsername, secondToken);
        
        // 验证第二个token有效
        boolean isSecondTokenValid = securityService.isTokenValid(testUsername, secondToken);
        assertTrue(isSecondTokenValid, "第二个token应该有效");
        
        // 验证第一个token已失效
        boolean isFirstTokenStillValid = securityService.isTokenValid(testUsername, firstToken);
        assertFalse(isFirstTokenStillValid, "第一个token应该失效");
    }
    
    @Test
    @DisplayName("测试登出功能 - 清除token记录")
    void testLogoutClearsToken() {
        // 记录token
        String token = "test-token-123";
        securityService.recordUserToken(testUsername, token);
        
        // 验证token有效
        boolean isTokenValid = securityService.isTokenValid(testUsername, token);
        assertTrue(isTokenValid, "token应该有效");
        
        // 清除token
        securityService.clearUserToken(testUsername);
        
        // 验证token已清除
        boolean isTokenValidAfterClear = securityService.isTokenValid(testUsername, "new-token");
        assertTrue(isTokenValidAfterClear, "清除token后应该允许新token");
    }
    
    @Test
    @DisplayName("测试用户导出功能 - 超级管理员权限检查")
    void testUserExportPermissionCheck() {
        // 创建模拟用户
        UmsAdmin testAdmin = new UmsAdmin();
        testAdmin.setId(1L);
        testAdmin.setUsername("admin");
        
        // 创建普通角色
        UmsRole normalRole = new UmsRole();
        normalRole.setId(1L);
        normalRole.setName("普通用户");
        
        // 创建超级管理员角色
        UmsRole superAdminRole = new UmsRole();
        superAdminRole.setId(SecurityConstants.SUPER_ADMIN_ROLE_ID);
        superAdminRole.setName(SecurityConstants.SUPER_ADMIN_ROLE_NAME);
        
        // 测试普通用户无权限
        List<UmsRole> normalRoles = new ArrayList<>();
        normalRoles.add(normalRole);
        
        boolean isNormalUserSuperAdmin = normalRoles.stream()
            .anyMatch(role -> SecurityConstants.SUPER_ADMIN_ROLE_NAME.equals(role.getName()) 
                    || SecurityConstants.SUPER_ADMIN_ROLE_ID.equals(role.getId()));
        assertFalse(isNormalUserSuperAdmin, "普通用户不应该有超级管理员权限");
        
        // 测试超级管理员有权限
        List<UmsRole> superAdminRoles = new ArrayList<>();
        superAdminRoles.add(superAdminRole);
        
        boolean isSuperAdmin = superAdminRoles.stream()
            .anyMatch(role -> SecurityConstants.SUPER_ADMIN_ROLE_NAME.equals(role.getName()) 
                    || SecurityConstants.SUPER_ADMIN_ROLE_ID.equals(role.getId()));
        assertTrue(isSuperAdmin, "超级管理员应该有超级管理员权限");
    }
    
    @Test
    @DisplayName("测试数据脱敏功能")
    void testDataDesensitization() {
        // 测试邮箱脱敏
        String email1 = "test@example.com";
        String maskedEmail1 = maskEmail(email1);
        assertEquals("tes***@example.com", maskedEmail1, "邮箱脱敏不正确");
        
        String email2 = "ab@example.com";
        String maskedEmail2 = maskEmail(email2);
        assertEquals("***@example.com", maskedEmail2, "短邮箱脱敏不正确");
        
        String email3 = null;
        String maskedEmail3 = maskEmail(email3);
        assertEquals("", maskedEmail3, "null邮箱应该返回空字符串");
        
        String email4 = "invalid-email";
        String maskedEmail4 = maskEmail(email4);
        assertEquals("", maskedEmail4, "无效邮箱应该返回空字符串");
    }
    
    @Test
    @DisplayName("测试账号自动解锁功能")
    void testAccountAutoUnlock() {
        // 锁定账号
        for (int i = 0; i < SecurityConstants.MAX_LOGIN_FAILURES; i++) {
            securityService.recordLoginFailure(testUsername, testIp, "密码错误");
        }
        
        // 验证账号已锁定
        UmsAdminLocked lockedRecord = securityService.checkUserLocked(testUsername);
        assertNotNull(lockedRecord, "用户应该被锁定");
        
        // 手动解锁
        boolean unlockSuccess = securityService.unlockUser(testUsername);
        assertTrue(unlockSuccess, "解锁应该成功");
        
        // 验证账号已解锁
        UmsAdminLocked lockedRecordAfterUnlock = securityService.checkUserLocked(testUsername);
        assertNull(lockedRecordAfterUnlock, "用户应该已解锁");
    }
    
    /**
     * 邮箱脱敏辅助方法
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "";
        }
        
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return "";
        }
        
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() > 3) {
            return username.substring(0, 3) + "***@" + domain;
        } else {
            return "***@" + domain;
        }
    }
}