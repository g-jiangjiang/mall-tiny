package com.macro.mall.tiny.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.domain.PmsSpecValue;
import com.macro.mall.tiny.service.PmsSpecValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PmsSpecValueController", description = "商品规格值管理")
@RestController
@RequestMapping("/specValue")
public class PmsSpecValueController {

    @Autowired
    private PmsSpecValueService specValueService;

    @Operation(summary = "根据类型ID获取规格值列表")
    @GetMapping("/listByTypeId/{typeId}")
    public CommonResult<List<PmsSpecValue>> listByTypeId(@PathVariable Long typeId) {
        List<PmsSpecValue> list = specValueService.listByTypeId(typeId);
        return CommonResult.success(list);
    }

    @Operation(summary = "创建规格值")
    @PostMapping("/create")
    public CommonResult<Void> create(@RequestBody PmsSpecValue specValue) {
        if (specValueService.save(specValue)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "批量创建规格值")
    @PostMapping("/batchCreate")
    public CommonResult<Void> batchCreate(@RequestBody List<PmsSpecValue> specValues) {
        if (specValueService.saveBatch(specValues)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新规格值")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody PmsSpecValue specValue) {
        specValue.setId(id);
        if (specValueService.updateById(specValue)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除规格值")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        if (specValueService.removeById(id)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
