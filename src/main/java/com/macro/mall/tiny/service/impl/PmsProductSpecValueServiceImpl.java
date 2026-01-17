package com.macro.mall.tiny.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.domain.PmsProductSpecValue;
import com.macro.mall.tiny.dto.PmsProductSpecValueParam;
import com.macro.mall.tiny.mapper.PmsProductSpecValueMapper;
import com.macro.mall.tiny.service.PmsProductSpecValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsProductSpecValueServiceImpl extends ServiceImpl<PmsProductSpecValueMapper, PmsProductSpecValue> implements PmsProductSpecValueService {

    @Override
    public Page<PmsProductSpecValue> list(Long specId, Integer pageNum, Integer pageSize) {
        Page<PmsProductSpecValue> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProductSpecValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsProductSpecValue::getSpecId, specId)
                .orderByAsc(PmsProductSpecValue::getSort);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean create(PmsProductSpecValueParam param, Long specId) {
        PmsProductSpecValue specValue = new PmsProductSpecValue();
        BeanUtils.copyProperties(param, specValue);
        specValue.setSpecId(specId);
        return save(specValue);
    }

    @Override
    public boolean update(Long id, PmsProductSpecValueParam param) {
        PmsProductSpecValue specValue = new PmsProductSpecValue();
        BeanUtils.copyProperties(param, specValue);
        specValue.setId(id);
        return updateById(specValue);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public PmsProductSpecValue getDetail(Long id) {
        return getById(id);
    }

    @Override
    public boolean batchCreate(List<PmsProductSpecValueParam> paramList, Long specId) {
        if (CollUtil.isEmpty(paramList)) return false;
        List<PmsProductSpecValue> specValueList = CollUtil.map(paramList, param -> {
            PmsProductSpecValue specValue = new PmsProductSpecValue();
            BeanUtils.copyProperties(param, specValue);
            specValue.setSpecId(specId);
            return specValue;
        }, true);
        return saveBatch(specValueList);
    }
}
