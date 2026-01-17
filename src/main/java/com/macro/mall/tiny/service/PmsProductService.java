package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.domain.PmsProduct;
import com.macro.mall.tiny.dto.PmsProductParam;

import java.util.List;

public interface PmsProductService extends IService<PmsProduct> {
    Page<PmsProduct> list(String name, Long categoryId, Integer pageNum, Integer pageSize);
    boolean create(PmsProductParam param);
    boolean update(Long id, PmsProductParam param);
    boolean delete(Long id);
    PmsProduct getDetail(Long id);
    List<PmsProduct> listAll();
}
