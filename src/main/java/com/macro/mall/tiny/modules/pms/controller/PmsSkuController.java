package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.dto.PmsSkuParam;
import com.macro.mall.tiny.modules.pms.model.PmsSku;
import com.macro.mall.tiny.modules.pms.service.PmsSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "PmsSkuController", description = "SKU商品管理")
@RequestMapping("/sku")
public class PmsSkuController {

    @Autowired
    private PmsSkuService skuService;

    @Operation(summary = "分页查询SKU商品")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsSku>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                    @RequestParam(value = "spuId", required = false) Long spuId,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<PmsSku> skuList = skuService.list(keyword, spuId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(skuList));
    }

    @Operation(summary = "添加SKU商品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@Validated @RequestBody PmsSkuParam skuParam) {
        boolean success = skuService.create(skuParam);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "批量添加SKU商品")
    @RequestMapping(value = "/batchCreate/{spuId}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult batchCreate(@PathVariable Long spuId, @RequestBody List<PmsSkuParam> skuParams) {
        boolean success = skuService.batchCreate(spuId, skuParams);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新SKU商品")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @Validated @RequestBody PmsSkuParam skuParam) {
        boolean success = skuService.update(id, skuParam);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除SKU商品")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = skuService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取SKU商品信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsSku> getItem(@PathVariable Long id) {
        PmsSku sku = skuService.getById(id);
        return CommonResult.success(sku);
    }
}
