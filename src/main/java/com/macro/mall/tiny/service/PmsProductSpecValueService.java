package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.domain.PmsProductSpecValue;
import com.macro.mall.tiny.dto.PmsProductSpecValueParam;

import java.util.List;

public interface PmsProductSpecValueService extends IService<PmsProductSpecValue> {
    Page<PmsProductSpecValue> list(Long specId, Integer pageNum, Integer pageSize);
    boolean create(PmsProductSpecValueParam param, Long specId);
    boolean update(Long id, PmsProductSpecValueParam param);
    boolean delete(Long id);
    PmsProductSpecValue getDetail(Long id);
    boolean batchCreate(List<PmsProductSpecValueParam> paramList, Long specId);
}
