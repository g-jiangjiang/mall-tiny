package com.macro.mall.tiny.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.domain.PmsCategory;
import com.macro.mall.tiny.service.PmsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PmsCategoryController", description = "商品类目管理")
@RestController
@RequestMapping("/category")
public class PmsCategoryController {

    @Autowired
    private PmsCategoryService categoryService;

    @Operation(summary = "获取所有类目树形列表")
    @GetMapping("/treeList")
    public CommonResult<List<PmsCategory>> treeList() {
        List<PmsCategory> list = categoryService.treeList();
        return CommonResult.success(list);
    }

    @Operation(summary = "创建类目")
    @PostMapping("/create")
    public CommonResult<Void> create(@RequestBody PmsCategory category) {
        if (categoryService.save(category)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "批量创建类目")
    @PostMapping("/batchCreate")
    public CommonResult<Void> batchCreate(@RequestBody List<PmsCategory> categories) {
        if (categoryService.saveBatch(categories)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新类目")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody PmsCategory category) {
        category.setId(id);
        if (categoryService.updateById(category)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除类目")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        if (categoryService.removeById(id)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
