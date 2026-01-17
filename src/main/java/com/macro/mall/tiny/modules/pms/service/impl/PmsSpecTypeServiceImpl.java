package com.macro.mall.tiny.modules.pms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.dto.PmsSpecTypeParam;
import com.macro.mall.tiny.modules.pms.dto.PmsSpecValueParam;
import com.macro.mall.tiny.modules.pms.mapper.PmsSpecTypeMapper;
import com.macro.mall.tiny.modules.pms.mapper.PmsSpecValueMapper;
import com.macro.mall.tiny.modules.pms.model.PmsSpecType;
import com.macro.mall.tiny.modules.pms.model.PmsSpecValue;
import com.macro.mall.tiny.modules.pms.service.PmsSpecTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsSpecTypeServiceImpl extends ServiceImpl<PmsSpecTypeMapper, PmsSpecType> implements PmsSpecTypeService {

    @Autowired
    private PmsSpecValueMapper specValueMapper;

    @Override
    public List<PmsSpecType> listWithValues() {
        List<PmsSpecType> specTypes = list();
        if (CollUtil.isEmpty(specTypes)) {
            return new ArrayList<>();
        }
        for (PmsSpecType specType : specTypes) {
            LambdaQueryWrapper<PmsSpecValue> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PmsSpecValue::getSpecTypeId, specType.getId());
            wrapper.orderByAsc(PmsSpecValue::getSort);
            List<PmsSpecValue> specValues = specValueMapper.selectList(wrapper);
            specType.setSpecValues(specValues);
        }
        return specTypes;
    }

    @Override
    @Transactional
    public boolean create(PmsSpecTypeParam specTypeParam) {
        PmsSpecType specType = new PmsSpecType();
        BeanUtils.copyProperties(specTypeParam, specType);
        save(specType);

        List<PmsSpecValueParam> specValueParams = specTypeParam.getSpecValueList();
        if (CollUtil.isNotEmpty(specValueParams)) {
            List<PmsSpecValue> specValues = specValueParams.stream().map(param -> {
                PmsSpecValue specValue = new PmsSpecValue();
                BeanUtils.copyProperties(param, specValue);
                specValue.setSpecTypeId(specType.getId());
                return specValue;
            }).collect(Collectors.toList());
            specValues.forEach(specValueMapper::insert);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean update(Long id, PmsSpecTypeParam specTypeParam) {
        PmsSpecType specType = new PmsSpecType();
        BeanUtils.copyProperties(specTypeParam, specType);
        specType.setId(id);
        updateById(specType);

        LambdaQueryWrapper<PmsSpecValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsSpecValue::getSpecTypeId, id);
        specValueMapper.delete(wrapper);

        List<PmsSpecValueParam> specValueParams = specTypeParam.getSpecValueList();
        if (CollUtil.isNotEmpty(specValueParams)) {
            List<PmsSpecValue> specValues = specValueParams.stream().map(param -> {
                PmsSpecValue specValue = new PmsSpecValue();
                BeanUtils.copyProperties(param, specValue);
                specValue.setSpecTypeId(id);
                return specValue;
            }).collect(Collectors.toList());
            specValues.forEach(specValueMapper::insert);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        LambdaQueryWrapper<PmsSpecValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsSpecValue::getSpecTypeId, id);
        specValueMapper.delete(wrapper);
        return removeById(id);
    }
}
