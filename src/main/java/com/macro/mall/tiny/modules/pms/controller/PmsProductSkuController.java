package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.domain.PmsProductSku;
import com.macro.mall.tiny.modules.pms.dto.PmsProductSkuParam;
import com.macro.mall.tiny.modules.pms.service.PmsProductSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pms/sku")
@Tag(name = "PmsProductSkuController", description = "商品SKU管理")
public class PmsProductSkuController {

    private final PmsProductSkuService productSkuService;

    public PmsProductSkuController(PmsProductSkuService productSkuService) {
        this.productSkuService = productSkuService;
    }

    @Operation(summary = "分页查询SKU列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsProductSku>> list(
            @RequestParam Long productId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<PmsProductSku> page = productSkuService.list(productId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "获取SKU详情")
    @GetMapping("/get/{id}")
    public CommonResult<PmsProductSku> getDetail(@PathVariable Long id) {
        PmsProductSku sku = productSkuService.getDetail(id);
        if (sku == null) {
            return CommonResult.failed("SKU不存在");
        }
        return CommonResult.success(sku);
    }

    @Operation(summary = "创建SKU")
    @PostMapping("/create")
    public CommonResult<Void> create(@Validated @RequestBody PmsProductSkuParam param, @RequestParam Long productId) {
        boolean success = productSkuService.create(param, productId);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @Operation(summary = "批量创建SKU")
    @PostMapping("/batchCreate")
    public CommonResult<Void> batchCreate(@Validated @RequestBody List<PmsProductSkuParam> paramList, @RequestParam Long productId) {
        boolean success = productSkuService.batchCreate(paramList, productId);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新SKU")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @Validated @RequestBody PmsProductSkuParam param) {
        boolean success = productSkuService.update(id, param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除SKU")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = productSkuService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }
}
