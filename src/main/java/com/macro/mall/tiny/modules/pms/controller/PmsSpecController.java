package com.macro.mall.tiny.modules.pms.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.domain.PmsSpecType;
import com.macro.mall.tiny.modules.pms.domain.PmsSpecValue;
import com.macro.mall.tiny.modules.pms.service.PmsSpecTypeService;
import com.macro.mall.tiny.modules.pms.service.PmsSpecValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "PmsSpecController", description = "规格管理")
@RequestMapping("/spec")
public class PmsSpecController {

    @Autowired
    private PmsSpecTypeService specTypeService;

    @Autowired
    private PmsSpecValueService specValueService;

    @Operation(summary = "获取所有规格类型")
    @GetMapping("/type/list")
    public CommonResult<List<PmsSpecType>> listAllTypes() {
        List<PmsSpecType> types = specTypeService.listAll();
        return CommonResult.success(types);
    }

    @Operation(summary = "创建规格类型")
    @PostMapping("/type/create")
    public CommonResult<Void> createType(@RequestBody PmsSpecType specType) {
        if (specTypeService.save(specType)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新规格类型")
    @PutMapping("/type/update/{id}")
    public CommonResult<Void> updateType(@PathVariable Long id, @RequestBody PmsSpecType specType) {
        specType.setId(id);
        if (specTypeService.updateById(specType)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除规格类型")
    @DeleteMapping("/type/delete/{id}")
    public CommonResult<Void> deleteType(@PathVariable Long id) {
        if (specTypeService.removeById(id)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @Operation(summary = "根据规格类型ID获取规格值列表")
    @GetMapping("/value/list/{typeId}")
    public CommonResult<List<PmsSpecValue>> listValuesByType(@PathVariable Long typeId) {
        List<PmsSpecValue> values = specValueService.listByTypeId(typeId);
        return CommonResult.success(values);
    }

    @Operation(summary = "创建规格值")
    @PostMapping("/value/create")
    public CommonResult<Void> createValue(@RequestBody PmsSpecValue specValue) {
        if (specValueService.save(specValue)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新规格值")
    @PutMapping("/value/update/{id}")
    public CommonResult<Void> updateValue(@PathVariable Long id, @RequestBody PmsSpecValue specValue) {
        specValue.setId(id);
        if (specValueService.updateById(specValue)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除规格值")
    @DeleteMapping("/value/delete/{id}")
    public CommonResult<Void> deleteValue(@PathVariable Long id) {
        if (specValueService.removeById(id)) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }
}