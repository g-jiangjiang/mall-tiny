package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.entity.PmsSku;

import java.util.List;

public interface PmsSkuService extends IService<PmsSku> {

    boolean create(PmsSku sku);

    boolean update(Long id, PmsSku sku);

    boolean delete(Long id);

    Page<PmsSku> list(Long spuId, Integer pageNum, Integer pageSize);

    List<PmsSku> getBySpuId(Long spuId);

    boolean batchCreate(List<PmsSku> skuList);

    boolean batchUpdate(List<PmsSku> skuList);

    boolean updateStock(Long id, Integer stock);

    boolean updatePrice(Long id, java.math.BigDecimal price);
}
