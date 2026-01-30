package com.macro.mall.tiny.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.entity.PmsSpecType;
import com.macro.mall.tiny.service.PmsSpecTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "PmsSpecTypeController", description = "规格类型管理")
@RequestMapping("/specType")
public class PmsSpecTypeController {

    @Autowired
    private PmsSpecTypeService specTypeService;

    @Operation(summary = "创建规格类型")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody PmsSpecType specType) {
        boolean success = specTypeService.create(specType);
        return success ? CommonResult.success(true) : CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新规格类型")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody PmsSpecType specType) {
        boolean success = specTypeService.update(id, specType);
        return success ? CommonResult.success(true) : CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除规格类型")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = specTypeService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed("删除失败");
    }

    @Operation(summary = "获取规格类型详情")
    @GetMapping("/{id}")
    public CommonResult<PmsSpecType> getById(@PathVariable Long id) {
        PmsSpecType specType = specTypeService.getById(id);
        return CommonResult.success(specType);
    }

    @Operation(summary = "分页查询规格类型列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsSpecType>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PmsSpecType> page = specTypeService.list(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "查询所有规格类型")
    @GetMapping("/listAll")
    public CommonResult<List<PmsSpecType>> listAll() {
        List<PmsSpecType> specTypes = specTypeService.listAll();
        return CommonResult.success(specTypes);
    }

    @Operation(summary = "批量创建规格类型")
    @PostMapping("/batchCreate")
    public CommonResult<Boolean> batchCreate(@RequestBody List<PmsSpecType> specTypes) {
        boolean success = specTypeService.batchCreate(specTypes);
        return success ? CommonResult.success(true) : CommonResult.failed("批量创建失败");
    }

    @Operation(summary = "批量更新规格类型")
    @PostMapping("/batchUpdate")
    public CommonResult<Boolean> batchUpdate(@RequestBody List<PmsSpecType> specTypes) {
        boolean success = specTypeService.batchUpdate(specTypes);
        return success ? CommonResult.success(true) : CommonResult.failed("批量更新失败");
    }
}
