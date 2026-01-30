package com.macro.mall.tiny.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.entity.PmsSpecValue;
import com.macro.mall.tiny.mapper.PmsSpecValueMapper;
import com.macro.mall.tiny.service.PmsSpecValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PmsSpecValueServiceImpl extends ServiceImpl<PmsSpecValueMapper, PmsSpecValue> implements PmsSpecValueService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(PmsSpecValue specValue) {
        specValue.setCreateTime(LocalDateTime.now());
        specValue.setUpdateTime(LocalDateTime.now());
        return this.save(specValue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long id, PmsSpecValue specValue) {
        specValue.setId(id);
        specValue.setUpdateTime(LocalDateTime.now());
        return this.updateById(specValue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public Page<PmsSpecValue> list(Long specTypeId, Integer pageNum, Integer pageSize) {
        Page<PmsSpecValue> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsSpecValue> wrapper = new LambdaQueryWrapper<>();
        if (specTypeId != null) {
            wrapper.eq(PmsSpecValue::getSpecTypeId, specTypeId);
        }
        wrapper.orderByAsc(PmsSpecValue::getSort);
        return this.page(page, wrapper);
    }

    @Override
    public List<PmsSpecValue> getBySpecTypeId(Long specTypeId) {
        return this.list(new LambdaQueryWrapper<PmsSpecValue>()
                .eq(PmsSpecValue::getSpecTypeId, specTypeId)
                .orderByAsc(PmsSpecValue::getSort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreate(List<PmsSpecValue> specValues) {
        LocalDateTime now = LocalDateTime.now();
        for (PmsSpecValue specValue : specValues) {
            specValue.setCreateTime(now);
            specValue.setUpdateTime(now);
        }
        return this.saveBatch(specValues);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<PmsSpecValue> specValues) {
        LocalDateTime now = LocalDateTime.now();
        for (PmsSpecValue specValue : specValues) {
            specValue.setUpdateTime(now);
        }
        return this.updateBatchById(specValues);
    }
}
