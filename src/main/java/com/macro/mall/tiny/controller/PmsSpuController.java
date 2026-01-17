package com.macro.mall.tiny.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.domain.PmsSpu;
import com.macro.mall.tiny.service.PmsSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PmsSpuController", description = "商品SPU管理")
@RestController
@RequestMapping("/spu")
public class PmsSpuController {

    @Autowired
    private PmsSpuService spuService;

    @Operation(summary = "分页查询SPU列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsSpu>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PmsSpu> page = spuService.list(keyword, categoryId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "根据ID获取SPU详情")
    @GetMapping("/{id}")
    public CommonResult<PmsSpu> getById(@PathVariable Long id) {
        PmsSpu spu = spuService.getById(id);
        if (spu != null) {
            return CommonResult.success(spu);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "创建SPU")
    @PostMapping("/create")
    public CommonResult<Void> create(@RequestBody PmsSpu spu) {
        if (spuService.save(spu)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新SPU")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody PmsSpu spu) {
        spu.setId(id);
        if (spuService.updateById(spu)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除SPU")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        if (spuService.removeById(id)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
