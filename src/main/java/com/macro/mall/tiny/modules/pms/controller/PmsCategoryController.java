package com.macro.mall.tiny.modules.pms.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.dto.PmsCategoryNode;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import com.macro.mall.tiny.modules.pms.service.PmsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品类目管理
 */
@RestController
@Tag(name = "PmsCategoryController", description = "商品类目管理")
@RequestMapping("/category")
public class PmsCategoryController {

    @Autowired
    private PmsCategoryService categoryService;

    @Operation(summary = "获取类目树")
    @GetMapping("/tree")
    public CommonResult<List<PmsCategoryNode>> tree() {
        List<PmsCategoryNode> tree = categoryService.tree();
        return CommonResult.success(tree);
    }

    @Operation(summary = "根据父ID获取子类目")
    @GetMapping("/listByParentId")
    public CommonResult<List<PmsCategory>> listByParentId(
            @Parameter(description = "父类目ID", example = "0") @RequestParam(defaultValue = "0") Long parentId) {
        List<PmsCategory> list = categoryService.listByParentId(parentId);
        return CommonResult.success(list);
    }

    @Operation(summary = "创建类目")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody PmsCategory category) {
        boolean success = categoryService.create(category);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "更新类目")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody PmsCategory category) {
        boolean success = categoryService.update(id, category);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "删除类目")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = categoryService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "获取类目详情")
    @GetMapping("/{id}")
    public CommonResult<PmsCategory> getById(@PathVariable Long id) {
        PmsCategory category = categoryService.getById(id);
        return CommonResult.success(category);
    }
}
