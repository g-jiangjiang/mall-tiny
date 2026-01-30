package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.dto.UmsDepartmentNode;
import com.macro.mall.tiny.modules.ums.model.UmsDepartment;
import com.macro.mall.tiny.modules.ums.service.UmsDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "UmsDepartmentController", description = "组织架构管理")
@RequestMapping("/department")
public class UmsDepartmentController {

    @Autowired
    private UmsDepartmentService departmentService;

    @Operation(summary = "创建部门")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UmsDepartment department) {
        try {
            boolean success = departmentService.create(department);
            if (success) {
                return CommonResult.success(null);
            }
            return CommonResult.failed("创建失败");
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @Operation(summary = "更新部门")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsDepartment department) {
        try {
            boolean success = departmentService.update(id, department);
            if (success) {
                return CommonResult.success(null);
            }
            return CommonResult.failed("更新失败");
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @Operation(summary = "删除部门")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        try {
            boolean success = departmentService.delete(id);
            if (success) {
                return CommonResult.success(null);
            }
            return CommonResult.failed("删除失败");
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @Operation(summary = "根据父级ID获取子部门列表")
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsDepartment>> listByParentId(@PathVariable Long parentId) {
        List<UmsDepartment> list = departmentService.listByParentId(parentId);
        return CommonResult.success(list);
    }

    @Operation(summary = "获取组织架构树")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsDepartmentNode>> treeList() {
        List<UmsDepartmentNode> treeList = departmentService.treeList();
        return CommonResult.success(treeList);
    }

    @Operation(summary = "获取指定部门详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsDepartment> getById(@PathVariable Long id) {
        UmsDepartment department = departmentService.getById(id);
        return CommonResult.success(department);
    }

    @Operation(summary = "获取当前用户可配置的组织架构树")
    @RequestMapping(value = "/configurableTree/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsDepartmentNode>> getConfigurableTree(@PathVariable Long adminId) {
        List<UmsDepartmentNode> treeList = departmentService.getConfigurableDepartmentTree(adminId);
        return CommonResult.success(treeList);
    }

    @Operation(summary = "获取指定用户的组织架构树")
    @RequestMapping(value = "/userTree/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsDepartmentNode>> getUserTree(@PathVariable Long adminId) {
        List<UmsDepartmentNode> treeList = departmentService.getDepartmentListByAdminId(adminId);
        return CommonResult.success(treeList);
    }
}
