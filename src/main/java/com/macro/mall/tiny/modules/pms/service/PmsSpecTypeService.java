package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.dto.PmsSpecTypeParam;
import com.macro.mall.tiny.modules.pms.model.PmsSpecType;

import java.util.List;

public interface PmsSpecTypeService extends IService<PmsSpecType> {

    List<PmsSpecType> listWithValues();

    boolean create(PmsSpecTypeParam specTypeParam);

    boolean update(Long id, PmsSpecTypeParam specTypeParam);

    boolean delete(Long id);
}
