package com.macro.mall.tiny.modules.pms.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.dto.PmsSpecTypeParam;
import com.macro.mall.tiny.modules.pms.model.PmsSpecType;
import com.macro.mall.tiny.modules.pms.service.PmsSpecTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "PmsSpecTypeController", description = "商品规格类型管理")
@RequestMapping("/specType")
public class PmsSpecTypeController {

    @Autowired
    private PmsSpecTypeService specTypeService;

    @Operation(summary = "获取所有规格类型及其规格值")
    @RequestMapping(value = "/listWithValues", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsSpecType>> listWithValues() {
        List<PmsSpecType> specTypeList = specTypeService.listWithValues();
        return CommonResult.success(specTypeList);
    }

    @Operation(summary = "添加规格类型")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@Validated @RequestBody PmsSpecTypeParam specTypeParam) {
        boolean success = specTypeService.create(specTypeParam);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新规格类型")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @Validated @RequestBody PmsSpecTypeParam specTypeParam) {
        boolean success = specTypeService.update(id, specTypeParam);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除规格类型")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = specTypeService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取规格类型详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsSpecType> getItem(@PathVariable Long id) {
        PmsSpecType specType = specTypeService.getById(id);
        return CommonResult.success(specType);
    }
}
