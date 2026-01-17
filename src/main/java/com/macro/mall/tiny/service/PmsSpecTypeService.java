package com.macro.mall.tiny.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.domain.PmsSpecType;

import java.util.List;

public interface PmsSpecTypeService extends IService<PmsSpecType> {
    List<PmsSpecType> listAll();
}
