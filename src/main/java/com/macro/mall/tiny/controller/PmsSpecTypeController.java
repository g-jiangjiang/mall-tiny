package com.macro.mall.tiny.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.domain.PmsSpecType;
import com.macro.mall.tiny.service.PmsSpecTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PmsSpecTypeController", description = "商品规格类型管理")
@RestController
@RequestMapping("/specType")
public class PmsSpecTypeController {

    @Autowired
    private PmsSpecTypeService specTypeService;

    @Operation(summary = "获取所有规格类型")
    @GetMapping("/listAll")
    public CommonResult<List<PmsSpecType>> listAll() {
        List<PmsSpecType> list = specTypeService.listAll();
        return CommonResult.success(list);
    }

    @Operation(summary = "创建规格类型")
    @PostMapping("/create")
    public CommonResult<Void> create(@RequestBody PmsSpecType specType) {
        if (specTypeService.save(specType)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "批量创建规格类型")
    @PostMapping("/batchCreate")
    public CommonResult<Void> batchCreate(@RequestBody List<PmsSpecType> specTypes) {
        if (specTypeService.saveBatch(specTypes)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新规格类型")
    @PostMapping("/update/{id}")
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody PmsSpecType specType) {
        specType.setId(id);
        if (specTypeService.updateById(specType)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除规格类型")
    @PostMapping("/delete/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        if (specTypeService.removeById(id)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
