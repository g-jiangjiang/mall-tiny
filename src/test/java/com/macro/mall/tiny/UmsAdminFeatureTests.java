package com.macro.mall.tiny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import com.macro.mall.tiny.modules.ums.service.UmsAdminService;
import com.macro.mall.tiny.common.service.RedisService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UmsAdminFeatureTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UmsAdminService adminService;

    @Autowired
    private RedisService redisService;

    private static final String TEST_USERNAME = "admin";
    private static final String TEST_PASSWORD = "123456";
    private static final String WRONG_PASSWORD = "wrong";

    // 登录防爆破测试
    @Test
    @Order(1)
    public void testLoginBruteForceProtection() {
        // 清除可能存在的锁定状态
        String lockKey = "login:lock:" + TEST_USERNAME;
        String failCountKey = "login:fail:count:" + TEST_USERNAME;
        redisService.del(lockKey);
        redisService.del(failCountKey);

        // 测试1: 连续5次错误登录
        System.out.println("测试1: 连续5次错误登录");
        for (int i = 1; i <= 5; i++) {
            ResponseEntity<String> response = login(TEST_USERNAME, WRONG_PASSWORD);
            assertEquals(HttpStatus.OK, response.getStatusCode());

            CommonResult<Map<String, String>> result = JSON.parseObject(
                    response.getBody(),
                    new TypeReference<CommonResult<Map<String, String>>>() {}
            );

            if (i < 5) {
                assertEquals(500, result.getCode());
                System.out.println("第" + i + "次登录失败: " + result.getMessage());
            } else {
                assertEquals(403, result.getCode());
                assertTrue(result.getMessage().contains("锁定"));
                System.out.println("第" + i + "次登录失败: " + result.getMessage());
            }
        }

        // 测试2: 第6次登录应该返回锁定
        System.out.println("\n测试2: 第6次登录应该返回锁定");
        ResponseEntity<String> lockedResponse = login(TEST_USERNAME, TEST_PASSWORD);
        assertEquals(HttpStatus.OK, lockedResponse.getStatusCode());

        CommonResult<Map<String, String>> lockedResult = JSON.parseObject(
                lockedResponse.getBody(),
                new TypeReference<CommonResult<Map<String, String>>>() {}
        );

        assertEquals(403, lockedResult.getCode());
        assertTrue(lockedResult.getMessage().contains("锁定"));
        System.out.println("锁定验证通过: " + lockedResult.getMessage());

        System.out.println("\n登录防爆破测试通过!");
    }

    // 互斥登录测试
    @Test
    @Order(2)
    public void testExclusiveLogin() {
        // 清除可能存在的锁定状态和token
        String lockKey = "login:lock:" + TEST_USERNAME;
        String tokenKey = "user:token:" + TEST_USERNAME;
        redisService.del(lockKey);
        redisService.del(tokenKey);

        // 测试1: 第一次登录获取token
        System.out.println("测试1: 第一次登录获取token");
        ResponseEntity<String> firstResponse = login(TEST_USERNAME, TEST_PASSWORD);
        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());

        CommonResult<Map<String, String>> firstResult = JSON.parseObject(
                firstResponse.getBody(),
                new TypeReference<CommonResult<Map<String, String>>>() {}
        );

        assertEquals(200, firstResult.getCode());
        String firstToken = firstResult.getData().get("token");
        assertNotNull(firstToken);
        System.out.println("第一次登录成功，token: " + firstToken.substring(0, 20) + "...");

        // 测试2: 使用第一个token验证
        System.out.println("\n测试2: 使用第一个token验证");
        boolean isValidFirst = validateToken(firstToken);
        assertTrue(isValidFirst);
        System.out.println("第一个token验证通过");

        // 测试3: 第二次登录获取新token
        System.out.println("\n测试3: 第二次登录获取新token");
        ResponseEntity<String> secondResponse = login(TEST_USERNAME, TEST_PASSWORD);
        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());

        CommonResult<Map<String, String>> secondResult = JSON.parseObject(
                secondResponse.getBody(),
                new TypeReference<CommonResult<Map<String, String>>>() {}
        );

        assertEquals(200, secondResult.getCode());
        String secondToken = secondResult.getData().get("token");
        assertNotNull(secondToken);
        assertNotEquals(firstToken, secondToken);
        System.out.println("第二次登录成功，新token: " + secondToken.substring(0, 20) + "...");

        // 测试4: 第一个token应该失效
        System.out.println("\n测试4: 第一个token应该失效");
        boolean isValidFirstAgain = validateToken(firstToken);
        assertFalse(isValidFirstAgain);
        System.out.println("第一个token已失效");

        // 测试5: 第二个token应该有效
        System.out.println("\n测试5: 第二个token应该有效");
        boolean isValidSecond = validateToken(secondToken);
        assertTrue(isValidSecond);
        System.out.println("第二个token验证通过");

        // 测试6: 登出后token失效
        System.out.println("\n测试6: 登出后token失效");
        logout(secondToken);
        boolean isValidAfterLogout = validateToken(secondToken);
        assertFalse(isValidAfterLogout);
        System.out.println("登出后token已失效");

        System.out.println("\n互斥登录测试通过!");
    }

    // 导出用户列表测试
    @Test
    @Order(3)
    public void testExportUserList() {
        // 先登录获取token
        ResponseEntity<String> loginResponse = login(TEST_USERNAME, TEST_PASSWORD);
        CommonResult<Map<String, String>> loginResult = JSON.parseObject(
                loginResponse.getBody(),
                new TypeReference<CommonResult<Map<String, String>>>() {}
        );

        String token = loginResult.getData().get("token");

        // 导出用户列表
        System.out.println("测试: 导出用户列表");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> exportResponse = restTemplate.exchange(
                "/admin/export",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, exportResponse.getStatusCode());

        CommonResult<List<Map<String, Object>>> exportResult = JSON.parseObject(
                exportResponse.getBody(),
                new TypeReference<CommonResult<List<Map<String, Object>>>>() {}
        );

        assertEquals(200, exportResult.getCode());
        List<Map<String, Object>> userList = exportResult.getData();
        assertNotNull(userList);
        assertFalse(userList.isEmpty());

        // 检查数据脱敏
        System.out.println("\n用户列表:");
        for (Map<String, Object> user : userList) {
            String username = (String) user.get("username");
            String email = (String) user.get("email");
            
            System.out.println("用户名: " + username + ", 邮箱: " + email);
            
            // 验证邮箱脱敏格式: 前两位 + *** + @domain
            if (email != null && email.contains("@")) {
                assertTrue(email.length() > 6); // 至少: xx***@d
                assertTrue(email.contains("***@"));
                assertEquals(3, email.indexOf("***")); // 前两位后是***
            }
        }

        System.out.println("\n导出用户列表测试通过!");
    }

    private ResponseEntity<String> login(String username, String password) {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username);
        loginData.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(loginData, headers);

        return restTemplate.postForEntity("/admin/login", entity, String.class);
    }

    private boolean validateToken(String token) {
        try {
            Thread.sleep(100); // 避免并发问题
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return adminService.validateTokenExclusively(token);
    }

    private void logout(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.postForEntity("/admin/logout", entity, String.class);
    }
}