package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.domain.PmsCategory;
import com.macro.mall.tiny.modules.pms.dto.PmsCategoryParam;
import com.macro.mall.tiny.modules.pms.service.PmsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pms/category")
@Tag(name = "PmsCategoryController", description = "商品类目管理")
public class PmsCategoryController {

    private final PmsCategoryService categoryService;

    public PmsCategoryController(PmsCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "分页查询类目列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsCategory>> list(
            @RequestParam(required = false, defaultValue = "0") Long parentId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<PmsCategory> page = categoryService.list(parentId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "获取所有类目列表")
    @GetMapping("/listAll")
    public CommonResult<List<PmsCategory>> listAll() {
        List<PmsCategory> list = categoryService.listAll();
        return CommonResult.success(list);
    }

    @Operation(summary = "获取类目详情")
    @GetMapping("/get/{id}")
    public CommonResult<PmsCategory> getDetail(@PathVariable Long id) {
        PmsCategory category = categoryService.getDetail(id);
        if (category == null) {
            return CommonResult.failed("类目不存在");
        }
        return CommonResult.success(category);
    }

    @Operation(summary = "创建类目")
    @PostMapping("/create")
    public CommonResult<Void> create(@Validated @RequestBody PmsCategoryParam param) {
        boolean success = categoryService.create(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新类目")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @Validated @RequestBody PmsCategoryParam param) {
        boolean success = categoryService.update(id, param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除类目")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = categoryService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败，存在子类目");
    }
}
