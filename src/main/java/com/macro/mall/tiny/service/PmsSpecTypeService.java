package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.entity.PmsSpecType;

import java.util.List;

public interface PmsSpecTypeService extends IService<PmsSpecType> {

    boolean create(PmsSpecType specType);

    boolean update(Long id, PmsSpecType specType);

    boolean delete(Long id);

    Page<PmsSpecType> list(Integer pageNum, Integer pageSize);

    List<PmsSpecType> listAll();

    boolean batchCreate(List<PmsSpecType> specTypes);

    boolean batchUpdate(List<PmsSpecType> specTypes);
}
