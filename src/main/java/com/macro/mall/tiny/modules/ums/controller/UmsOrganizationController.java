package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.dto.OrganizationTreeNode;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;
import com.macro.mall.tiny.modules.ums.service.UmsOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 组织架构管理控制器
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
@RestController
@RequestMapping("/organization")
@Tag(name = "组织架构管理", description = "组织架构相关接口")
public class UmsOrganizationController {

    @Autowired
    private UmsOrganizationService organizationService;

    @Operation(summary = "获取组织架构树")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('ums:organization:read')")
    public CommonResult<List<OrganizationTreeNode>> getOrganizationTree(@RequestParam Long adminId) {
        List<OrganizationTreeNode> tree = organizationService.getOrganizationTree(adminId);
        return CommonResult.success(tree);
    }

    @Operation(summary = "获取当前管理员有权限的组织架构树")
    @GetMapping("/tree/current")
    @PreAuthorize("hasAuthority('ums:organization:read')")
    public CommonResult<List<OrganizationTreeNode>> getCurrentAdminOrganizationTree() {
        // 这里需要从SecurityContext中获取当前登录的管理员ID
        // 暂时使用固定值，实际项目中应该从Spring Security上下文中获取
        Long currentAdminId = 1L; // TODO: 从SecurityContext中获取当前管理员ID
        List<OrganizationTreeNode> tree = organizationService.getOrganizationTree(currentAdminId);
        return CommonResult.success(tree);
    }

    @Operation(summary = "创建组织架构")
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ums:organization:create')")
    public CommonResult<Boolean> create(@RequestBody UmsOrganization organization) {
        boolean success = organizationService.create(organization);
        if (success) {
            return CommonResult.success(true);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新组织架构")
    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ums:organization:update')")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody UmsOrganization organization) {
        boolean success = organizationService.update(id, organization);
        if (success) {
            return CommonResult.success(true);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除组织架构")
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ums:organization:delete')")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = organizationService.delete(id);
        if (success) {
            return CommonResult.success(true);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取组织架构详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ums:organization:read')")
    public CommonResult<UmsOrganization> getItem(@PathVariable Long id) {
        UmsOrganization organization = organizationService.getItem(id);
        return CommonResult.success(organization);
    }

    @Operation(summary = "初始化默认组织架构")
    @PostMapping("/init")
    @PreAuthorize("hasAuthority('ums:organization:create')")
    public CommonResult<Boolean> initDefaultOrganization() {
        organizationService.initDefaultOrganization();
        return CommonResult.success(true);
    }
}