package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.dto.PmsSkuParam;
import com.macro.mall.tiny.modules.pms.model.PmsSku;

public interface PmsSkuService extends IService<PmsSku> {

    Page<PmsSku> list(String keyword, Long spuId, Integer pageSize, Integer pageNum);

    boolean create(PmsSkuParam skuParam);

    boolean update(Long id, PmsSkuParam skuParam);

    boolean delete(Long id);

    boolean batchCreate(Long spuId, java.util.List<PmsSkuParam> skuParams);
}
