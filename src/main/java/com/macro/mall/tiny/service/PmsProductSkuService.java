package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.domain.PmsProductSku;
import com.macro.mall.tiny.dto.PmsProductSkuParam;

import java.util.List;

public interface PmsProductSkuService extends IService<PmsProductSku> {
    Page<PmsProductSku> list(Long productId, Integer pageNum, Integer pageSize);
    boolean create(PmsProductSkuParam param, Long productId);
    boolean update(Long id, PmsProductSkuParam param);
    boolean delete(Long id);
    PmsProductSku getDetail(Long id);
    boolean batchCreate(List<PmsProductSkuParam> paramList, Long productId);
}
