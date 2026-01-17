package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.domain.PmsSpecType;

import java.util.List;

public interface PmsSpecTypeService extends IService<PmsSpecType> {
    List<PmsSpecType> listAll();
}