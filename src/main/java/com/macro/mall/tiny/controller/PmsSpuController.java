package com.macro.mall.tiny.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.entity.PmsSpu;
import com.macro.mall.tiny.service.PmsSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "PmsSpuController", description = "商品SPU管理")
@RequestMapping("/spu")
public class PmsSpuController {

    @Autowired
    private PmsSpuService spuService;

    @Operation(summary = "创建SPU")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody PmsSpu spu) {
        boolean success = spuService.create(spu);
        return success ? CommonResult.success(true) : CommonResult.failed("创建失败");
    }

    @Operation(summary = "更新SPU")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody PmsSpu spu) {
        boolean success = spuService.update(id, spu);
        return success ? CommonResult.success(true) : CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除SPU")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        boolean success = spuService.delete(id);
        return success ? CommonResult.success(true) : CommonResult.failed("删除失败");
    }

    @Operation(summary = "获取SPU详情")
    @GetMapping("/{id}")
    public CommonResult<PmsSpu> getById(@PathVariable Long id) {
        PmsSpu spu = spuService.getDetail(id);
        return CommonResult.success(spu);
    }

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

    @Operation(summary = "更新上架状态")
    @PostMapping("/updatePublishStatus/{id}")
    public CommonResult<Boolean> updatePublishStatus(@PathVariable Long id, @RequestParam Integer publishStatus) {
        boolean success = spuService.updatePublishStatus(id, publishStatus);
        return success ? CommonResult.success(true) : CommonResult.failed("更新失败");
    }

    @Operation(summary = "更新审核状态")
    @PostMapping("/updateVerifyStatus/{id}")
    public CommonResult<Boolean> updateVerifyStatus(@PathVariable Long id, @RequestParam Integer verifyStatus) {
        boolean success = spuService.updateVerifyStatus(id, verifyStatus);
        return success ? CommonResult.success(true) : CommonResult.failed("更新失败");
    }
}
