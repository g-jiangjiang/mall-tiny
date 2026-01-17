package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.domain.PmsCategory;

import java.util.List;

public interface PmsCategoryService extends IService<PmsCategory> {
    List<PmsCategory> treeList();
}
