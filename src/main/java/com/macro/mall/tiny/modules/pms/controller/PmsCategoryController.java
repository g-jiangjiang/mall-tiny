package com.macro.mall.tiny.modules.pms.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.dto.PmsCategoryParam;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import com.macro.mall.tiny.modules.pms.service.PmsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "PmsCategoryController", description = "商品类目管理")
@RequestMapping("/category")
public class PmsCategoryController {

    @Autowired
    private PmsCategoryService categoryService;

    @Operation(summary = "获取所有类目及其子类目")
    @RequestMapping(value = "/listWithChildren", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsCategory>> listWithChildren() {
        List<PmsCategory> categoryList = categoryService.listWithChildren();
        return CommonResult.success(categoryList);
    }

    @Operation(summary = "添加类目")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@Validated @RequestBody PmsCategoryParam categoryParam) {
        boolean success = categoryService.create(categoryParam);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "更新类目")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @Validated @RequestBody PmsCategoryParam categoryParam) {
        boolean success = categoryService.update(id, categoryParam);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "删除类目")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = categoryService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @Operation(summary = "获取类目详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsCategory> getItem(@PathVariable Long id) {
        PmsCategory category = categoryService.getById(id);
        return CommonResult.success(category);
    }
}
