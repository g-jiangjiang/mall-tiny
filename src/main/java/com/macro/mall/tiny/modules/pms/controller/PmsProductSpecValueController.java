package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.domain.PmsProductSpecValue;
import com.macro.mall.tiny.modules.pms.dto.PmsProductSpecValueParam;
import com.macro.mall.tiny.modules.pms.service.PmsProductSpecValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pms/specValue")
@Tag(name = "PmsProductSpecValueController", description = "商品规格值管理")
public class PmsProductSpecValueController {

    private final PmsProductSpecValueService productSpecValueService;

    public PmsProductSpecValueController(PmsProductSpecValueService productSpecValueService) {
        this.productSpecValueService = productSpecValueService;
    }

    @Operation(summary = "分页查询规格值列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsProductSpecValue>> list(
            @RequestParam(required = false) Long specId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<PmsProductSpecValue> page = productSpecValueService.list(specId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "获取规格值详情")
    @GetMapping("/get/{id}")
    public CommonResult<PmsProductSpecValue> getDetail(@PathVariable Long id) {
        PmsProductSpecValue specValue = productSpecValueService.getDetail(id);
        if (specValue == null) {
            return CommonResult.failed("规格值不存在");
        }
        return CommonResult.success(specValue);
    }

    @Operation(summary = "创建规格值")
    @PostMapping("/create")
    public CommonResult<Void> create(@Validated @RequestBody PmsProductSpecValueParam param, @RequestParam Long specId) {
        boolean success = productSpecValueService.create(param, specId);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @Operation(summary = "批量创建规格值")
    @PostMapping("/batchCreate")
    public CommonResult<Void> batchCreate(@Validated @RequestBody List<PmsProductSpecValueParam> paramList, @RequestParam Long specId) {
        boolean success = productSpecValueService.batchCreate(paramList, specId);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("批量创建失败");
    }

    @Operation(summary = "更新规格值")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @Validated @RequestBody PmsProductSpecValueParam param) {
        boolean success = productSpecValueService.update(id, param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除规格值")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = productSpecValueService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }
}
