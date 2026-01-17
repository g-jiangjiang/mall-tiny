package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.dto.PmsSpuParam;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;

public interface PmsSpuService extends IService<PmsSpu> {

    Page<PmsSpu> list(String keyword, Integer pageSize, Integer pageNum);

    PmsSpu getDetail(Long id);

    boolean create(PmsSpuParam spuParam);

    boolean update(Long id, PmsSpuParam spuParam);

    boolean delete(Long id);
}
