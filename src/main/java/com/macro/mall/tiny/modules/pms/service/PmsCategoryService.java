package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.dto.PmsCategoryParam;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;

import java.util.List;

public interface PmsCategoryService extends IService<PmsCategory> {

    List<PmsCategory> listWithChildren();

    boolean create(PmsCategoryParam categoryParam);

    boolean update(Long id, PmsCategoryParam categoryParam);

    boolean delete(Long id);
}
