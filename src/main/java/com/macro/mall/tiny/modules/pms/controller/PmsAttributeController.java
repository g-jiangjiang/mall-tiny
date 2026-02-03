package com.macro.mall.tiny.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsAttribute;
import com.macro.mall.tiny.modules.pms.model.PmsAttributeValue;
import com.macro.mall.tiny.modules.pms.service.PmsAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品规格管理
 */
@RestController
@Tag(name = "PmsAttributeController", description = "商品规格管理")
@RequestMapping("/attribute")
public class PmsAttributeController {

    @Autowired
    private PmsAttributeService attributeService;

    @Operation(summary = "分页查询规格")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsAttribute>> list(
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "规格类型：0->规格；1->参数") @RequestParam(required = false) Integer type,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum) {
        Page<PmsAttribute> page = attributeService.list(keyword, type, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @Operation(summary = "创建规格")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody PmsAttribute attribute) {
        boolean success = attributeService.create(attribute);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "更新规格")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody PmsAttribute attribute) {
        boolean success = attributeService.update(id, attribute);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "删除规格")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = attributeService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "获取规格详情")
    @GetMapping("/{id}")
    public CommonResult<PmsAttribute> getById(@PathVariable Long id) {
        PmsAttribute attribute = attributeService.getDetail(id);
        return CommonResult.success(attribute);
    }

    @Operation(summary = "根据SPU ID获取规格列表")
    @GetMapping("/listBySpuId/{spuId}")
    public CommonResult<List<PmsAttribute>> listBySpuId(@PathVariable Long spuId) {
        List<PmsAttribute> list = attributeService.listBySpuId(spuId);
        return CommonResult.success(list);
    }

    // 规格值管理

    @Operation(summary = "添加规格值")
    @PostMapping("/value/create")
    public CommonResult<Boolean> addAttributeValue(@RequestBody PmsAttributeValue attributeValue) {
        boolean success = attributeService.addAttributeValue(attributeValue);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "更新规格值")
    @PostMapping("/value/update/{id}")
    public CommonResult<Boolean> updateAttributeValue(@PathVariable Long id, @RequestBody PmsAttributeValue attributeValue) {
        boolean success = attributeService.updateAttributeValue(id, attributeValue);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "删除规格值")
    @PostMapping("/value/delete/{id}")
    public CommonResult<Boolean> deleteAttributeValue(@PathVariable Long id) {
        boolean success = attributeService.deleteAttributeValue(id);
        return success ? CommonResult.success(true) : CommonResult.failed();
    }

    @Operation(summary = "获取规格值列表")
    @GetMapping("/value/list/{attributeId}")
    public CommonResult<List<PmsAttributeValue>> listAttributeValues(@PathVariable Long attributeId) {
        List<PmsAttributeValue> list = attributeService.listAttributeValues(attributeId);
        return CommonResult.success(list);
    }
}
