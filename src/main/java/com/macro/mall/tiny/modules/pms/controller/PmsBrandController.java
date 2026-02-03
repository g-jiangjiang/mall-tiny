package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsBrand;
import com.macro.mall.tiny.modules.pms.service.PmsBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品品牌管理
 */
@RestController
@Tag(name = "PmsBrandController", description = "商品品牌管理")
@RequestMapping("/brand")
public class PmsBrandController {

    @Autowired
    private PmsBrandService brandService;

    @Operation(summary = "分页查询品牌")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsBrand>> list(
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum) {
        Page<PmsBrand> page = brandService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "获取所有品牌")
    @GetMapping("/listAll")
    public CommonResult<List<PmsBrand>> listAll() {
        List<PmsBrand> list = brandService.listAll();
        return CommonResult.success(list);
    }

    @Operation(summary = "创建品牌")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody PmsBrand brand) {
        boolean success = brandService.create(brand);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "更新品牌")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody PmsBrand brand) {
        boolean success = brandService.update(id, brand);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "删除品牌")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = brandService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "批量删除品牌")
    @PostMapping("/delete/batch")
    public CommonResult<Boolean> deleteBatch(@RequestParam List<Long> ids) {
        boolean success = brandService.deleteBatch(ids);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "获取品牌详情")
    @GetMapping("/{id}")
    public CommonResult<PmsBrand> getById(@PathVariable Long id) {
        PmsBrand brand = brandService.getById(id);
        return CommonResult.success(brand);
    }
}
