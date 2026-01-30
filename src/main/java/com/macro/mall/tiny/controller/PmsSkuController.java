package com.macro.mall.tiny.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.entity.PmsSku;
import com.macro.mall.tiny.service.PmsSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Tag(name = "PmsSkuController", description = "商品SKU管理")
@RequestMapping("/sku")
public class PmsSkuController {

    @Autowired
    private PmsSkuService skuService;

    @Operation(summary = "创建SKU")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody PmsSku sku) {
        boolean success = skuService.create(sku);
        return success ? CommonResult.success(true) : CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新SKU")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody PmsSku sku) {
        boolean success = skuService.update(id, sku);
        return success ? CommonResult.success(true) : CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除SKU")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = skuService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed("删除失败");
    }

    @Operation(summary = "获取SKU详情")
    @GetMapping("/{id}")
    public CommonResult<PmsSku> getById(@PathVariable Long id) {
        PmsSku sku = skuService.getById(id);
        return CommonResult.success(sku);
    }

    @Operation(summary = "分页查询SKU列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsSku>> list(
            @RequestParam(required = false) Long spuId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PmsSku> page = skuService.list(spuId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "根据SPU ID查询SKU列表")
    @GetMapping("/list/{spuId}")
    public CommonResult<List<PmsSku>> getBySpuId(@PathVariable Long spuId) {
        List<PmsSku> skuList = skuService.getBySpuId(spuId);
        return CommonResult.success(skuList);
    }

    @Operation(summary = "批量创建SKU")
    @PostMapping("/batchCreate")
    public CommonResult<Boolean> batchCreate(@RequestBody List<PmsSku> skuList) {
        boolean success = skuService.batchCreate(skuList);
        return success ? CommonResult.success(true) : CommonResult.failed("批量创建失败");
    }

    @Operation(summary = "批量更新SKU")
    @PostMapping("/batchUpdate")
    public CommonResult<Boolean> batchUpdate(@RequestBody List<PmsSku> skuList) {
        boolean success = skuService.batchUpdate(skuList);
        return success ? CommonResult.success(true) : CommonResult.failed("批量更新失败");
    }

    @Operation(summary = "更新库存")
    @PostMapping("/updateStock/{id}")
    public CommonResult<Boolean> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        boolean success = skuService.updateStock(id, stock);
        return success ? CommonResult.success(true) : CommonResult.failed("更新失败");
    }

    @Operation(summary = "更新价格")
    @PostMapping("/updatePrice/{id}")
    public CommonResult<Boolean> updatePrice(@PathVariable Long id, @RequestParam BigDecimal price) {
        boolean success = skuService.updatePrice(id, price);
        return success ? CommonResult.success(true) : CommonResult.failed("更新失败");
    }
}
