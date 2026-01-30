package com.macro.mall.tiny.modules.ums.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macro.mall.tiny.common.SecurityConstants;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.service.UmsAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UmsAdminController测试类
 * 测试用户导出和登出功能
 * Created by macro on 2024/01/30.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UmsAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private UmsAdminService adminService;
    
    private UmsAdmin testAdmin;
    private List<UmsRole> normalRoles;
    private List<UmsRole> superAdminRoles;
    
    @BeforeEach
    void setUp() {
        // 创建测试用户
        testAdmin = new UmsAdmin();
        testAdmin.setId(1L);
        testAdmin.setUsername("testuser");
        testAdmin.setPassword("$2a$10$BV5U..."); // 加密密码
        testAdmin.setStatus(1);
        testAdmin.setEmail("test@example.com");
        
        // 创建普通角色
        UmsRole normalRole = new UmsRole();
        normalRole.setId(1L);
        normalRole.setName("普通用户");
        
        // 创建超级管理员角色
        UmsRole superAdminRole = new UmsRole();
        superAdminRole.setId(SecurityConstants.SUPER_ADMIN_ROLE_ID);
        superAdminRole.setName("超级管理员");
        
        normalRoles = new ArrayList<>();
        normalRoles.add(normalRole);
        
        superAdminRoles = new ArrayList<>();
        superAdminRoles.add(superAdminRole);
        
        // 模拟服务方法
        when(adminService.getAdminByUsername(anyString())).thenReturn(testAdmin);
    }
    
    @Test
    @DisplayName("测试用户导出功能 - 超级管理员权限")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testExportUserListWithSuperAdmin() throws Exception {
        // 设置超级管理员角色
        when(adminService.getRoleList(anyLong())).thenReturn(superAdminRoles);
        
        // 模拟分页查询结果
        List<UmsAdmin> adminList = new ArrayList<>();
        adminList.add(testAdmin);
        
        // 模拟分页结果
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<UmsAdmin> mockPage = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<UmsAdmin>(1, 10, 1);
        mockPage.setRecords(adminList);
        
        when(adminService.list(anyString(), anyInt(), anyInt())).thenReturn(mockPage);
        
        // 执行导出请求
        MvcResult result = mockMvc.perform(get("/admin/export")
                .param("keyword", "test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        MockHttpServletResponse response = result.getResponse();
        
        // 验证响应头
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    response.getContentType());
        assertTrue(response.getHeader("Content-Disposition").contains("attachment; filename="));
        assertTrue(response.getHeader("Content-Disposition").contains("用户列表.xlsx"));
    }
    
    @Test
    @DisplayName("测试用户导出功能 - 普通用户无权限")
    @WithMockUser(username = "user", roles = {"USER"})
    void testExportUserListWithNormalUser() throws Exception {
        // 设置普通用户角色
        when(adminService.getRoleList(anyLong())).thenReturn(normalRoles);
        
        // 执行导出请求
        mockMvc.perform(get("/admin/export")
                .param("keyword", "test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("测试登出功能")
    @WithMockUser(username = "testuser")
    void testLogout() throws Exception {
        // 执行登出请求
        mockMvc.perform(post("/admin/logout")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("测试未认证用户访问导出功能")
    void testExportWithoutAuthentication() throws Exception {
        // 执行导出请求
        mockMvc.perform(get("/admin/export")
                .param("keyword", "test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}