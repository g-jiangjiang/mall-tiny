package com.macro.mall.tiny.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.entity.PmsSpecValue;
import com.macro.mall.tiny.service.PmsSpecValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "PmsSpecValueController", description = "规格值管理")
@RequestMapping("/specValue")
public class PmsSpecValueController {

    @Autowired
    private PmsSpecValueService specValueService;

    @Operation(summary = "创建规格值")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody PmsSpecValue specValue) {
        boolean success = specValueService.create(specValue);
        return success ? CommonResult.success(true) : CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新规格值")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody PmsSpecValue specValue) {
        boolean success = specValueService.update(id, specValue);
        return success ? CommonResult.success(true) : CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除规格值")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = specValueService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed("删除失败");
    }

    @Operation(summary = "获取规格值详情")
    @GetMapping("/{id}")
    public CommonResult<PmsSpecValue> getById(@PathVariable Long id) {
        PmsSpecValue specValue = specValueService.getById(id);
        return CommonResult.success(specValue);
    }

    @Operation(summary = "分页查询规格值列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsSpecValue>> list(
            @RequestParam(required = false) Long specTypeId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PmsSpecValue> page = specValueService.list(specTypeId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "根据规格类型ID查询规格值列表")
    @GetMapping("/list/{specTypeId}")
    public CommonResult<List<PmsSpecValue>> getBySpecTypeId(@PathVariable Long specTypeId) {
        List<PmsSpecValue> specValues = specValueService.getBySpecTypeId(specTypeId);
        return CommonResult.success(specValues);
    }

    @Operation(summary = "批量创建规格值")
    @PostMapping("/batchCreate")
    public CommonResult<Boolean> batchCreate(@RequestBody List<PmsSpecValue> specValues) {
        boolean success = specValueService.batchCreate(specValues);
        return success ? CommonResult.success(true) : CommonResult.failed("批量创建失败");
    }

    @Operation(summary = "批量更新规格值")
    @PostMapping("/batchUpdate")
    public CommonResult<Boolean> batchUpdate(@RequestBody List<PmsSpecValue> specValues) {
        boolean success = specValueService.batchUpdate(specValues);
        return success ? CommonResult.success(true) : CommonResult.failed("批量更新失败");
    }
}
