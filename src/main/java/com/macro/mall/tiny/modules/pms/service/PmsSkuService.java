package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.domain.PmsSku;

import java.util.List;

public interface PmsSkuService extends IService<PmsSku> {
    Page<PmsSku> list(Long spuId, String code, Integer pageNum, Integer pageSize);
    boolean batchCreate(List<PmsSku> skus);
}