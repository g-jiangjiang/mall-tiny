package com.macro.mall.tiny;

import com.macro.mall.tiny.modules.ums.dto.UmsAdminExcelDto;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.service.UmsAdminLoginAttemptService;
import com.macro.mall.tiny.modules.ums.service.UmsAdminService;
import com.macro.mall.tiny.modules.ums.service.UmsTokenStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SecurityFeatureTests {

    @Autowired
    private UmsAdminLoginAttemptService loginAttemptService;

    @Autowired
    private UmsTokenStoreService tokenStoreService;

    @Autowired
    private UmsAdminService adminService;

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_TOKEN = "testToken123";

    @BeforeEach
    void setUp() {
        loginAttemptService.loginSucceeded(TEST_USERNAME);
        tokenStoreService.removeToken(TEST_USERNAME);
    }

    @Test
    void testLoginAttemptNotLockedInitially() {
        assertFalse(loginAttemptService.isLocked(TEST_USERNAME));
    }

    @Test
    void testLoginFailedIncreasesAttempts() {
        loginAttemptService.loginFailed(TEST_USERNAME);
        assertEquals(1, loginAttemptService.getFailedAttempts(TEST_USERNAME));
    }

    @Test
    void testLoginSucceededClearsAttempts() {
        loginAttemptService.loginFailed(TEST_USERNAME);
        loginAttemptService.loginSucceeded(TEST_USERNAME);
        assertEquals(0, loginAttemptService.getFailedAttempts(TEST_USERNAME));
    }

    @Test
    void testMultipleFailedAttempts() {
        for (int i = 0; i < 3; i++) {
            loginAttemptService.loginFailed(TEST_USERNAME);
        }
        assertEquals(3, loginAttemptService.getFailedAttempts(TEST_USERNAME));
        assertFalse(loginAttemptService.isLocked(TEST_USERNAME));
    }

    @Test
    void testLockAfterMaxAttempts() {
        for (int i = 0; i < 5; i++) {
            loginAttemptService.loginFailed(TEST_USERNAME);
        }
        assertTrue(loginAttemptService.isLocked(TEST_USERNAME));
        assertTrue(loginAttemptService.getRemainingLockTime(TEST_USERNAME) > 0);
    }

    @Test
    void testRemainingLockTime() {
        for (int i = 0; i < 5; i++) {
            loginAttemptService.loginFailed(TEST_USERNAME);
        }
        long remainingTime = loginAttemptService.getRemainingLockTime(TEST_USERNAME);
        assertTrue(remainingTime > 0 && remainingTime <= 30);
    }

    @Test
    void testTokenStore() {
        tokenStoreService.storeToken(TEST_USERNAME, TEST_TOKEN);
        assertEquals(TEST_TOKEN, tokenStoreService.getCurrentToken(TEST_USERNAME));
    }

    @Test
    void testTokenValidation() {
        tokenStoreService.storeToken(TEST_USERNAME, TEST_TOKEN);
        assertTrue(tokenStoreService.isValidToken(TEST_USERNAME, TEST_TOKEN));
        assertFalse(tokenStoreService.isValidToken(TEST_USERNAME, "invalidToken"));
    }

    @Test
    void testTokenReplacement() {
        String oldToken = "oldToken";
        String newToken = "newToken";
        
        tokenStoreService.storeToken(TEST_USERNAME, oldToken);
        assertTrue(tokenStoreService.isValidToken(TEST_USERNAME, oldToken));
        
        tokenStoreService.storeToken(TEST_USERNAME, newToken);
        assertFalse(tokenStoreService.isValidToken(TEST_USERNAME, oldToken));
        assertTrue(tokenStoreService.isValidToken(TEST_USERNAME, newToken));
    }

    @Test
    void testTokenRemoval() {
        tokenStoreService.storeToken(TEST_USERNAME, TEST_TOKEN);
        tokenStoreService.removeToken(TEST_USERNAME);
        assertNull(tokenStoreService.getCurrentToken(TEST_USERNAME));
    }

    @Test
    void testMutualExclusiveLogin() {
        String firstToken = "firstSessionToken";
        String secondToken = "secondSessionToken";
        
        tokenStoreService.storeToken(TEST_USERNAME, firstToken);
        assertTrue(tokenStoreService.isValidToken(TEST_USERNAME, firstToken));
        
        tokenStoreService.storeToken(TEST_USERNAME, secondToken);
        assertFalse(tokenStoreService.isValidToken(TEST_USERNAME, firstToken));
        assertTrue(tokenStoreService.isValidToken(TEST_USERNAME, secondToken));
    }

    @Test
    void testIsSuperAdminWithNullRoles() {
        assertFalse(adminService.isSuperAdmin(999L));
    }

    @Test
    void testExportAdmins() {
        List<UmsAdminExcelDto> adminList = adminService.exportAllAdmins();
        assertNotNull(adminList);
        for (UmsAdminExcelDto admin : adminList) {
            assertNull(admin.getPassword());
            assertNotNull(admin.getUsername());
        }
    }

    @Test
    void testExportDataMasking() {
        List<UmsAdminExcelDto> adminList = adminService.exportAllAdmins();
        for (UmsAdminExcelDto admin : adminList) {
            assertNull(admin.getPassword());
        }
    }

    @Test
    void testExportContainsStatusDescription() {
        List<UmsAdminExcelDto> adminList = adminService.exportAllAdmins();
        for (UmsAdminExcelDto admin : adminList) {
            assertNotNull(admin.getStatusDesc());
            assertTrue(admin.getStatusDesc().equals("启用") || admin.getStatusDesc().equals("禁用"));
        }
    }
}
