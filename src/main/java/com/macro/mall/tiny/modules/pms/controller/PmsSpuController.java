package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.dto.PmsSpuParam;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;
import com.macro.mall.tiny.modules.pms.service.PmsSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品SPU管理
 */
@RestController
@Tag(name = "PmsSpuController", description = "商品SPU管理")
@RequestMapping("/spu")
public class PmsSpuController {

    @Autowired
    private PmsSpuService spuService;

    @Operation(summary = "分页查询SPU")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsSpu>> list(
            @Parameter(description = "类目ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "品牌ID") @RequestParam(required = false) Long brandId,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum) {
        Page<PmsSpu> page = spuService.list(categoryId, brandId, keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "创建SPU")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody PmsSpuParam spuParam) {
        boolean success = spuService.create(spuParam);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "更新SPU")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody PmsSpuParam spuParam) {
        boolean success = spuService.update(id, spuParam);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "删除SPU")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = spuService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "获取SPU详情")
    @GetMapping("/{id}")
    public CommonResult<PmsSpuParam> getById(@PathVariable Long id) {
        PmsSpuParam detail = spuService.getDetail(id);
        return CommonResult.success(detail);
    }

    @Operation(summary = "批量上架")
    @PostMapping("/publish")
    public CommonResult<Boolean> publish(@RequestParam List<Long> ids) {
        boolean success = spuService.updateStatusBatch(ids, 1);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "批量下架")
    @PostMapping("/unpublish")
    public CommonResult<Boolean> unpublish(@RequestParam List<Long> ids) {
        boolean success = spuService.updateStatusBatch(ids, 0);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }
}
