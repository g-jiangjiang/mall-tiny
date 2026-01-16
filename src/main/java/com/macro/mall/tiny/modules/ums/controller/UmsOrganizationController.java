package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;
import com.macro.mall.tiny.modules.ums.service.UmsOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织架构管理Controller
 *
 * @author macro
 * @since 2026-01-16
 */
@Controller
@Tag(name = "UmsOrganizationController", description = "组织架构管理")
@RequestMapping("/organization")
public class UmsOrganizationController {

    @Autowired
    private UmsOrganizationService organizationService;

    @Operation(summary = "添加组织")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UmsOrganization organization) {
        boolean success = organizationService.create(organization);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "修改组织")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsOrganization organization) {
        boolean success = organizationService.update(id, organization);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除组织")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = organizationService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("存在子组织，无法删除");
    }

    @Operation(summary = "获取指定组织信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsOrganization> getItem(@PathVariable Long id) {
        UmsOrganization organization = organizationService.get(id);
        return CommonResult.success(organization);
    }

    @Operation(summary = "获取组织树形结构")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsOrganization>> treeList() {
        List<UmsOrganization> list = organizationService.listTree();
        return CommonResult.success(list);
    }

    @Operation(summary = "获取指定组织的子组织")
    @RequestMapping(value = "/children/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsOrganization>> childrenList(@PathVariable Long parentId) {
        List<UmsOrganization> list = organizationService.listChildren(parentId);
        return CommonResult.success(list);
    }

    @Operation(summary = "分页查询组织")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsOrganization>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        CommonPage<UmsOrganization> organizationList = CommonPage.restPage(organizationService.list(keyword, pageSize, pageNum));
        return CommonResult.success(organizationList);
    }

}
