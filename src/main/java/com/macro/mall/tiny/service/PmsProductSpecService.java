package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.domain.PmsProductSpec;
import com.macro.mall.tiny.dto.PmsProductSpecParam;

import java.util.List;

public interface PmsProductSpecService extends IService<PmsProductSpec> {
    Page<PmsProductSpec> list(Long categoryId, Integer pageNum, Integer pageSize);
    boolean create(PmsProductSpecParam param);
    boolean update(Long id, PmsProductSpecParam param);
    boolean delete(Long id);
    PmsProductSpec getDetail(Long id);
    List<PmsProductSpec> listAll();
}
