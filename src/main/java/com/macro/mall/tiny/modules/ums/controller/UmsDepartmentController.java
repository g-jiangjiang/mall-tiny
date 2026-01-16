package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.dto.UmsDepartmentNode;
import com.macro.mall.tiny.modules.ums.dto.UmsDepartmentParam;
import com.macro.mall.tiny.modules.ums.model.UmsDepartment;
import com.macro.mall.tiny.modules.ums.service.UmsDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "UmsDepartmentController", description = "组织架构管理")
@RequestMapping("/department")
public class UmsDepartmentController {

    @Autowired
    private UmsDepartmentService departmentService;

    @Operation(summary = "获取组织架构树")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsDepartmentNode> tree() {
        UmsDepartmentNode tree = departmentService.buildTree();
        return CommonResult.success(tree);
    }

    @Operation(summary = "根据根节点ID获取组织架构树")
    @RequestMapping(value = "/tree/{rootId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsDepartmentNode> tree(@PathVariable Long rootId) {
        UmsDepartmentNode tree = departmentService.buildTree(rootId);
        return CommonResult.success(tree);
    }

    @Operation(summary = "创建部门")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@Validated @RequestBody UmsDepartmentParam param) {
        boolean success = departmentService.create(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新部门")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@Validated @RequestBody UmsDepartmentParam param) {
        boolean success = departmentService.update(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除部门")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = departmentService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取部门详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsDepartment> get(@PathVariable Long id) {
        UmsDepartment department = departmentService.getById(id);
        return CommonResult.success(department);
    }

    @Operation(summary = "获取子部门列表")
    @RequestMapping(value = "/children/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsDepartment>> children(@PathVariable Long parentId) {
        List<UmsDepartment> children = departmentService.listByParentId(parentId);
        return CommonResult.success(children);
    }

    @Operation(summary = "获取所有子部门ID（包含所有层级）")
    @RequestMapping(value = "/allChildren/{deptId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Long>> allChildren(@PathVariable Long deptId) {
        List<Long> childIds = departmentService.getAllChildDeptIds(deptId);
        return CommonResult.success(childIds);
    }

}
