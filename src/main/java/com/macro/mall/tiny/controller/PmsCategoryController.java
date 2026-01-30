package com.macro.mall.tiny.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.entity.PmsCategory;
import com.macro.mall.tiny.service.PmsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "PmsCategoryController", description = "商品类目管理")
@RequestMapping("/category")
public class PmsCategoryController {

    @Autowired
    private PmsCategoryService categoryService;

    @Operation(summary = "创建类目")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody PmsCategory category) {
        boolean success = categoryService.create(category);
        return success ? CommonResult.success(true) : CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新类目")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody PmsCategory category) {
        boolean success = categoryService.update(id, category);
        return success ? CommonResult.success(true) : CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除类目")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = categoryService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed("删除失败");
    }

    @Operation(summary = "获取类目详情")
    @GetMapping("/{id}")
    public CommonResult<PmsCategory> getById(@PathVariable Long id) {
        PmsCategory category = categoryService.getById(id);
        return CommonResult.success(category);
    }

    @Operation(summary = "分页查询类目列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsCategory>> list(
            @RequestParam(required = false) Long parentId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PmsCategory> page = categoryService.list(parentId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "获取树形类目列表")
    @GetMapping("/treeList")
    public CommonResult<List<PmsCategory>> treeList() {
        List<PmsCategory> categories = categoryService.listWithTree();
        return CommonResult.success(categories);
    }

    @Operation(summary = "批量创建类目")
    @PostMapping("/batchCreate")
    public CommonResult<Boolean> batchCreate(@RequestBody List<PmsCategory> categories) {
        boolean success = categoryService.batchCreate(categories);
        return success ? CommonResult.success(true) : CommonResult.failed("批量创建失败");
    }

    @Operation(summary = "批量更新类目")
    @PostMapping("/batchUpdate")
    public CommonResult<Boolean> batchUpdate(@RequestBody List<PmsCategory> categories) {
        boolean success = categoryService.batchUpdate(categories);
        return success ? CommonResult.success(true) : CommonResult.failed("批量更新失败");
    }
}
