package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.entity.PmsSpecValue;

import java.util.List;

public interface PmsSpecValueService extends IService<PmsSpecValue> {

    boolean create(PmsSpecValue specValue);

    boolean update(Long id, PmsSpecValue specValue);

    boolean delete(Long id);

    Page<PmsSpecValue> list(Long specTypeId, Integer pageNum, Integer pageSize);

    List<PmsSpecValue> getBySpecTypeId(Long specTypeId);

    boolean batchCreate(List<PmsSpecValue> specValues);

    boolean batchUpdate(List<PmsSpecValue> specValues);
}
