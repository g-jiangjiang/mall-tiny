package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.model.PmsBrand;

public interface PmsBrandService extends IService<PmsBrand> {

    Page<PmsBrand> list(String keyword, Integer pageSize, Integer pageNum);

    boolean create(PmsBrand brand);

    boolean update(Long id, PmsBrand brand);

    boolean delete(Long id);
}
