package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.model.UmsAdminOrganizationRelation;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;
import com.macro.mall.tiny.modules.ums.service.UmsAdminOrganizationRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员组织关系管理Controller
 *
 * @author macro
 * @since 2026-01-16
 */
@Controller
@Tag(name = "UmsAdminOrganizationRelationController", description = "管理员组织关系管理")
@RequestMapping("/admin/organization")
public class UmsAdminOrganizationRelationController {

    @Autowired
    private UmsAdminOrganizationRelationService adminOrganizationRelationService;

    @Operation(summary = "设置管理员组织权限")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@RequestParam Long adminId,
                               @RequestParam Long organizationId,
                               @RequestParam Integer scope) {
        boolean success = adminOrganizationRelationService.updateOrganization(adminId, organizationId, scope);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取管理员组织关系")
    @RequestMapping(value = "/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsAdminOrganizationRelation> getItem(@PathVariable Long adminId) {
        UmsAdminOrganizationRelation relation = adminOrganizationRelationService.getByAdminId(adminId);
        return CommonResult.success(relation);
    }

    @Operation(summary = "获取管理员可管理的组织列表")
    @RequestMapping(value = "/managed/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsOrganization>> getManagedOrganizations(@PathVariable Long adminId) {
        List<UmsOrganization> list = adminOrganizationRelationService.getManagedOrganizations(adminId);
        return CommonResult.success(list);
    }

}
