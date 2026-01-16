package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;
import com.macro.mall.tiny.modules.ums.service.UmsOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 组织架构管理
 * Created by macro on 2025-07-01
 */
@Controller
@Tag(name = "UmsOrganizationController", description = "组织架构管理")
@RequestMapping("/organization")
public class UmsOrganizationController {

    @Autowired
    private UmsOrganizationService organizationService;

    @Operation(summary = "获取组织架构树")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsOrganization>> treeList() {
        List<UmsOrganization> organizationList = organizationService.treeList();
        return CommonResult.success(organizationList);
    }

    @Operation(summary = "根据层级获取组织列表")
    @RequestMapping(value = "/listByLevel", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsOrganization>> listByLevel(@RequestParam("level") Integer level) {
        List<UmsOrganization> organizationList = organizationService.listByLevel(level);
        return CommonResult.success(organizationList);
    }

    @Operation(summary = "创建组织")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UmsOrganization organization) {
        // 检查层级限制
        if (!organizationService.checkLevelLimit(organization.getParentId())) {
            return CommonResult.failed("组织层级不能超过5层");
        }
        // 设置层级
        if (organization.getParentId() == null) {
            organization.setLevel(1);
        } else {
            UmsOrganization parent = organizationService.getById(organization.getParentId());
            if (parent != null) {
                organization.setLevel(parent.getLevel() + 1);
            }
        }
        organization.setCreateTime(new Date());
        organization.setUpdateTime(new Date());
        boolean success = organizationService.save(organization);
        if (success) {
            return CommonResult.success(organization);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "修改组织信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsOrganization organization) {
        organization.setId(id);
        organization.setUpdateTime(new Date());
        // 不允许修改层级和父级ID
        organization.setLevel(null);
        organization.setParentId(null);
        boolean success = organizationService.updateById(organization);
        if (success) {
            return CommonResult.success(organization);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除组织")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        // 检查是否有子组织
        List<Long> subOrgIds = organizationService.getSubOrganizationIds(id);
        if (subOrgIds.size() > 1) {
            return CommonResult.failed("存在子组织，不允许删除");
        }
        boolean success = organizationService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取指定组织详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsOrganization> detail(@PathVariable Long id) {
        UmsOrganization organization = organizationService.getById(id);
        return CommonResult.success(organization);
    }

    

    

}
