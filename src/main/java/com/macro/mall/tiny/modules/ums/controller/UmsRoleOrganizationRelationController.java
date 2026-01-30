package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.service.UmsRoleOrganizationRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色组织架构关联控制器
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
@RestController
@RequestMapping("/role/organization")
@Tag(name = "角色组织架构关联", description = "角色组织架构关联相关接口")
public class UmsRoleOrganizationRelationController {

    @Autowired
    private UmsRoleOrganizationRelationService roleOrganizationRelationService;

    @Operation(summary = "为角色分配组织架构权限")
    @PostMapping("/alloc")
    @PreAuthorize("hasAuthority('ums:role:update')")
    public CommonResult<Integer> allocOrganization(
            @RequestParam Long roleId,
            @RequestParam List<Long> organizationIds,
            @RequestParam Integer scope) {
        int count = roleOrganizationRelationService.allocOrganization(roleId, organizationIds, scope);
        return CommonResult.success(count);
    }

    @Operation(summary = "获取角色的组织架构权限")
    @GetMapping("/list/{roleId}")
    @PreAuthorize("hasAuthority('ums:role:read')")
    public CommonResult<List<Long>> listOrganization(@PathVariable Long roleId) {
        List<Long> organizationIds = roleOrganizationRelationService.getOrganizationIdsByRoleId(roleId);
        return CommonResult.success(organizationIds);
    }

    @Operation(summary = "获取管理员的组织架构权限")
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasAuthority('ums:admin:read')")
    public CommonResult<List<Long>> getAdminOrganizations(@PathVariable Long adminId) {
        List<Long> organizationIds = roleOrganizationRelationService.getOrganizationIdsByAdminId(adminId);
        return CommonResult.success(organizationIds);
    }
}