package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.domain.PmsCategory;
import com.macro.mall.tiny.dto.PmsCategoryParam;

import java.util.List;

public interface PmsCategoryService extends IService<PmsCategory> {
    Page<PmsCategory> list(Long parentId, Integer pageNum, Integer pageSize);
    boolean create(PmsCategoryParam param);
    boolean update(Long id, PmsCategoryParam param);
    boolean delete(Long id);
    PmsCategory getDetail(Long id);
    List<PmsCategory> listAll();
}
