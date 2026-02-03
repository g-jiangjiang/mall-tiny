package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.dto.UmsOrgNodeDto;
import com.macro.mall.tiny.modules.ums.model.UmsOrgNode;
import com.macro.mall.tiny.modules.ums.service.UmsOrgNodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织架构节点管理
 * Created by macro on 2024/01/01.
 */
@Controller
@Tag(name = "UmsOrgNodeController", description = "组织架构节点管理")
@RequestMapping("/orgNode")
public class UmsOrgNodeController {

    @Autowired
    private UmsOrgNodeService orgNodeService;

    @Operation(summary = "添加组织架构节点")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UmsOrgNode orgNode) {
        boolean success = orgNodeService.create(orgNode);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "修改组织架构节点")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsOrgNode orgNode) {
        boolean success = orgNodeService.update(id, orgNode);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除组织架构节点")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = orgNodeService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("该节点存在子节点，无法删除");
    }

    @Operation(summary = "获取组织架构树")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsOrgNodeDto>> getOrgTree() {
        List<UmsOrgNodeDto> orgTree = orgNodeService.getOrgTree();
        return CommonResult.success(orgTree);
    }

    @Operation(summary = "根据层级获取节点列表")
    @RequestMapping(value = "/listByLevel/{level}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsOrgNode>> listByLevel(@PathVariable Integer level) {
        List<UmsOrgNode> list = orgNodeService.listByLevel(level);
        return CommonResult.success(list);
    }

    @Operation(summary = "根据父级ID获取子节点")
    @RequestMapping(value = "/listByParentId/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsOrgNode>> listByParentId(@PathVariable Long parentId) {
        List<UmsOrgNode> list = orgNodeService.listByParentId(parentId);
        return CommonResult.success(list);
    }

    @Operation(summary = "初始化默认组织架构")
    @RequestMapping(value = "/initDefault", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult initDefaultOrg() {
        boolean success = orgNodeService.initDefaultOrg();
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("组织架构已存在，无需初始化");
    }

}
