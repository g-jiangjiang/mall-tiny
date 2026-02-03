package com.macro.mall.tiny.modules.pms.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.dto.PmsSkuBatchParam;
import com.macro.mall.tiny.modules.pms.model.PmsSku;
import com.macro.mall.tiny.modules.pms.service.PmsSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品SKU管理
 */
@RestController
@Tag(name = "PmsSkuController", description = "商品SKU管理")
@RequestMapping("/sku")
public class PmsSkuController {

    @Autowired
    private PmsSkuService skuService;

    @Operation(summary = "根据SPU ID获取SKU列表")
    @GetMapping("/listBySpuId/{spuId}")
    public CommonResult<List<PmsSku>> listBySpuId(@PathVariable Long spuId) {
        List<PmsSku> list = skuService.listBySpuId(spuId);
        return CommonResult.success(list);
    }

    @Operation(summary = "批量创建SKU")
    @PostMapping("/batchCreate/{spuId}")
    public CommonResult<Boolean> batchCreate(
            @PathVariable Long spuId,
            @RequestBody List<PmsSku> skuList) {
        boolean success = skuService.batchCreate(spuId, skuList);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "批量更新SKU")
    @PostMapping("/batchUpdate")
    public CommonResult<Boolean> batchUpdate(@RequestBody List<PmsSku> skuList) {
        boolean success = skuService.batchUpdate(skuList);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "批量配置SKU（创建、更新、删除混合操作）")
    @PostMapping("/batchConfig")
    public CommonResult<Boolean> batchConfig(@RequestBody PmsSkuBatchParam batchParam) {
        boolean success = skuService.batchConfig(batchParam);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "删除SKU")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = skuService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "批量删除SKU")
    @PostMapping("/delete/batch")
    public CommonResult<Boolean> deleteBatch(@RequestParam List<Long> ids) {
        boolean success = skuService.deleteBatch(ids);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "更新SKU库存")
    @PostMapping("/updateStock/{id}")
    public CommonResult<Boolean> updateStock(
            @PathVariable Long id,
            @Parameter(description = "库存数量", example = "100") @RequestParam Integer stock) {
        boolean success = skuService.updateStock(id, stock);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "更新SKU价格")
    @PostMapping("/updatePrice/{id}")
    public CommonResult<Boolean> updatePrice(
            @PathVariable Long id,
            @Parameter(description = "价格", example = "99.99") @RequestParam BigDecimal price) {
        boolean success = skuService.updatePrice(id, price);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "获取SKU详情")
    @GetMapping("/{id}")
    public CommonResult<PmsSku> getById(@PathVariable Long id) {
        PmsSku sku = skuService.getDetail(id);
        return CommonResult.success(sku);
    }
}
