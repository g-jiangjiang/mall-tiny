package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.domain.PmsCategory;

import java.util.List;

public interface PmsCategoryService extends IService<PmsCategory> {
    List<PmsCategory> treeList();
}