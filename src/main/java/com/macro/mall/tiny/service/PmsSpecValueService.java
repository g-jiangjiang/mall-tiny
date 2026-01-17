package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.domain.PmsSpecValue;

import java.util.List;

public interface PmsSpecValueService extends IService<PmsSpecValue> {
    List<PmsSpecValue> listByTypeId(Long typeId);
}
