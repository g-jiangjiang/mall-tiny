package com.macro.mall.tiny.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.domain.PmsSpecType;
import com.macro.mall.tiny.mapper.PmsSpecTypeMapper;
import com.macro.mall.tiny.service.PmsSpecTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsSpecTypeServiceImpl extends ServiceImpl<PmsSpecTypeMapper, PmsSpecType> implements PmsSpecTypeService {

    @Override
    public List<PmsSpecType> listAll() {
        return list();
    }
}
