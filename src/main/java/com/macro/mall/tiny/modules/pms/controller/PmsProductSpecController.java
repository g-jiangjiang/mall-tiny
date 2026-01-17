package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.domain.PmsProductSpec;
import com.macro.mall.tiny.modules.pms.dto.PmsProductSpecParam;
import com.macro.mall.tiny.modules.pms.service.PmsProductSpecService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pms/spec")
@Tag(name = "PmsProductSpecController", description = "商品规格类型管理")
public class PmsProductSpecController {

    private final PmsProductSpecService productSpecService;

    public PmsProductSpecController(PmsProductSpecService productSpecService) {
        this.productSpecService = productSpecService;
    }

    @Operation(summary = "分页查询规格类型列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsProductSpec>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<PmsProductSpec> page = productSpecService.list(categoryId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "获取所有规格类型列表")
    @GetMapping("/listAll")
    public CommonResult<List<PmsProductSpec>> listAll() {
        List<PmsProductSpec> list = productSpecService.listAll();
        return CommonResult.success(list);
    }

    @Operation(summary = "获取规格类型详情")
    @GetMapping("/get/{id}")
    public CommonResult<PmsProductSpec> getDetail(@PathVariable Long id) {
        PmsProductSpec spec = productSpecService.getDetail(id);
        if (spec == null) {
            return CommonResult.failed("规格类型不存在");
        }
        return CommonResult.success(spec);
    }

    @Operation(summary = "创建规格类型")
    @PostMapping("/create")
    public CommonResult<Void> create(@Validated @RequestBody PmsProductSpecParam param) {
        boolean success = productSpecService.create(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新规格类型")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @Validated @RequestBody PmsProductSpecParam param) {
        boolean success = productSpecService.update(id, param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除规格类型")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = productSpecService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }
}
