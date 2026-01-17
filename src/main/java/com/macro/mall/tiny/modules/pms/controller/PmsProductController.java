package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.domain.PmsProduct;
import com.macro.mall.tiny.modules.pms.dto.PmsProductParam;
import com.macro.mall.tiny.modules.pms.service.PmsProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pms/product")
@Tag(name = "PmsProductController", description = "商品SPU管理")
public class PmsProductController {

    private final PmsProductService productService;

    public PmsProductController(PmsProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "分页查询商品列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsProduct>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<PmsProduct> page = productService.list(name, categoryId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "获取所有商品列表")
    @GetMapping("/listAll")
    public CommonResult<List<PmsProduct>> listAll() {
        List<PmsProduct> list = productService.listAll();
        return CommonResult.success(list);
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/get/{id}")
    public CommonResult<PmsProduct> getDetail(@PathVariable Long id) {
        PmsProduct product = productService.getDetail(id);
        if (product == null) {
            return CommonResult.failed("商品不存在");
        }
        return CommonResult.success(product);
    }

    @Operation(summary = "创建商品")
    @PostMapping("/create")
    public CommonResult<Void> create(@Validated @RequestBody PmsProductParam param) {
        boolean success = productService.create(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新商品")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @Validated @RequestBody PmsProductParam param) {
        boolean success = productService.update(id, param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除商品")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = productService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }
}
