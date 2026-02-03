package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.dto.UmsOrgNodeDto;
import com.macro.mall.tiny.modules.ums.dto.UmsRoleOrgDto;
import com.macro.mall.tiny.modules.ums.service.UmsOrgNodeService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleOrgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色组织架构配置管理
 * Created by macro on 2024/01/01.
 */
@Controller
@Tag(name = "UmsRoleOrgController", description = "角色组织架构配置管理")
@RequestMapping("/roleOrg")
public class UmsRoleOrgController {

    @Autowired
    private UmsRoleOrgService roleOrgService;

    @Autowired
    private UmsOrgNodeService orgNodeService;

    @Operation(summary = "获取角色的组织架构配置")
    @RequestMapping(value = "/config/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsRoleOrgDto> getRoleOrgConfig(@PathVariable Long roleId) {
        UmsRoleOrgDto config = roleOrgService.getRoleOrgConfig(roleId);
        return CommonResult.success(config);
    }

    @Operation(summary = "配置角色组织架构")
    @RequestMapping(value = "/config/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult configRoleOrg(@PathVariable Long roleId,
                                       @RequestParam Integer scopeType,
                                       @RequestParam(required = false) List<Long> orgIds) {
        boolean success = roleOrgService.configRoleOrg(roleId, scopeType, orgIds);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取角色有权限的组织架构树")
    @RequestMapping(value = "/tree/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsOrgNodeDto>> getRoleOrgTree(@PathVariable Long roleId) {
        List<UmsOrgNodeDto> orgTree = orgNodeService.getOrgTreeByRoleId(roleId);
        return CommonResult.success(orgTree);
    }

    @Operation(summary = "检查角色是否有组织架构权限")
    @RequestMapping(value = "/checkPermission", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Boolean> checkPermission(@RequestParam Long roleId, @RequestParam Long orgId) {
        boolean hasPermission = roleOrgService.hasOrgPermission(roleId, orgId);
        return CommonResult.success(hasPermission);
    }

    @Operation(summary = "初始化角色的默认组织架构配置")
    @RequestMapping(value = "/initDefault/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult initDefaultConfig(@PathVariable Long roleId,
                                           @RequestParam(required = false, defaultValue = "0") Integer roleType) {
        boolean success = roleOrgService.initDefaultConfig(roleId, roleType);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "批量配置角色组织架构")
    @RequestMapping(value = "/batchConfig", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult batchConfigRoleOrg(@RequestParam List<Long> roleIds,
                                            @RequestParam Integer scopeType) {
        boolean success = roleOrgService.batchConfigRoleOrg(roleIds, scopeType);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

}
