package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.dto.RoleDataScopeParam;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.service.UmsRoleDataScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "UmsRoleDataScopeController", description = "角色数据范围管理")
@RequestMapping("/role/dataScope")
public class UmsRoleDataScopeController {

    @Autowired
    private UmsRoleDataScopeService roleDataScopeService;

    @Operation(summary = "更新角色数据范围")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@Validated @RequestBody RoleDataScopeParam param) {
        boolean success = roleDataScopeService.updateDataScope(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取角色数据范围部门ID列表")
    @RequestMapping(value = "/deptIds/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Long>> getDeptIds(@PathVariable Long roleId) {
        List<Long> deptIds = roleDataScopeService.getDataScopeDeptIds(roleId);
        return CommonResult.success(deptIds);
    }

    @Operation(summary = "获取角色详情（包含部门信息）")
    @RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsRole> getRole(@PathVariable Long roleId) {
        UmsRole role = roleDataScopeService.getRoleWithDept(roleId);
        return CommonResult.success(role);
    }

    @Operation(summary = "为角色分配部门（自定义数据范围）")
    @RequestMapping(value = "/allocDept", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult allocDept(@RequestParam Long roleId, @RequestParam List<Long> deptIds) {
        boolean success = roleDataScopeService.allocDept(roleId, deptIds);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取角色已分配的部门ID列表")
    @RequestMapping(value = "/allocatedDeptIds/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Long>> getAllocatedDeptIds(@PathVariable Long roleId) {
        List<Long> deptIds = roleDataScopeService.getAllocatedDeptIds(roleId);
        return CommonResult.success(deptIds);
    }

}
