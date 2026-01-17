package com.macro.mall.tiny.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.domain.PmsSku;
import com.macro.mall.tiny.service.PmsSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PmsSkuController", description = "商品SKU管理")
@RestController
@RequestMapping("/sku")
public class PmsSkuController {

    @Autowired
    private PmsSkuService skuService;

    @Operation(summary = "分页查询SKU列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsSku>> list(
            @RequestParam(required = false) Long spuId,
            @RequestParam(required = false) String code,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PmsSku> page = skuService.list(spuId, code, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "根据ID获取SKU详情")
    @GetMapping("/{id}")
    public CommonResult<PmsSku> getById(@PathVariable Long id) {
        PmsSku sku = skuService.getById(id);
        if (sku != null) {
            return CommonResult.success(sku);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "创建SKU")
    @PostMapping("/create")
    public CommonResult<Void> create(@RequestBody PmsSku sku) {
        if (skuService.save(sku)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "批量创建SKU")
    @PostMapping("/batchCreate")
    public CommonResult<Void> batchCreate(@RequestBody List<PmsSku> skus) {
        if (skuService.batchCreate(skus)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新SKU")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody PmsSku sku) {
        sku.setId(id);
        if (skuService.updateById(sku)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除SKU")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        if (skuService.removeById(id)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
