package com.macro.mall.tiny.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.domain.PmsSpecValue;
import com.macro.mall.tiny.mapper.PmsSpecValueMapper;
import com.macro.mall.tiny.service.PmsSpecValueService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsSpecValueServiceImpl extends ServiceImpl<PmsSpecValueMapper, PmsSpecValue> implements PmsSpecValueService {

    @Override
    public List<PmsSpecValue> listByTypeId(Long typeId) {
        return list(new LambdaQueryWrapper<PmsSpecValue>().eq(PmsSpecValue::getSpecTypeId, typeId));
    }
}
