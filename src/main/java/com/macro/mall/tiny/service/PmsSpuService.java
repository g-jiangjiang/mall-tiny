package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.domain.PmsSpu;

public interface PmsSpuService extends IService<PmsSpu> {
    Page<PmsSpu> list(String keyword, Long categoryId, Integer pageNum, Integer pageSize);
}
