package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.entity.PmsCategory;

import java.util.List;

public interface PmsCategoryService extends IService<PmsCategory> {

    boolean create(PmsCategory category);

    boolean update(Long id, PmsCategory category);

    boolean delete(Long id);

    List<PmsCategory> listWithTree();

    Page<PmsCategory> list(Long parentId, Integer pageNum, Integer pageSize);

    boolean batchCreate(List<PmsCategory> categories);

    boolean batchUpdate(List<PmsCategory> categories);
}
