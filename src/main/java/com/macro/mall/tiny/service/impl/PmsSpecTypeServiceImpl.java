package com.macro.mall.tiny.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.entity.PmsSpecType;
import com.macro.mall.tiny.mapper.PmsSpecTypeMapper;
import com.macro.mall.tiny.service.PmsSpecTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PmsSpecTypeServiceImpl extends ServiceImpl<PmsSpecTypeMapper, PmsSpecType> implements PmsSpecTypeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(PmsSpecType specType) {
        specType.setCreateTime(LocalDateTime.now());
        specType.setUpdateTime(LocalDateTime.now());
        return this.save(specType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long id, PmsSpecType specType) {
        specType.setId(id);
        specType.setUpdateTime(LocalDateTime.now());
        return this.updateById(specType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public Page<PmsSpecType> list(Integer pageNum, Integer pageSize) {
        Page<PmsSpecType> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsSpecType> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(PmsSpecType::getSort);
        return this.page(page, wrapper);
    }

    @Override
    public List<PmsSpecType> listAll() {
        return this.list(new LambdaQueryWrapper<PmsSpecType>()
                .orderByAsc(PmsSpecType::getSort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreate(List<PmsSpecType> specTypes) {
        LocalDateTime now = LocalDateTime.now();
        for (PmsSpecType specType : specTypes) {
            specType.setCreateTime(now);
            specType.setUpdateTime(now);
        }
        return this.saveBatch(specTypes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<PmsSpecType> specTypes) {
        LocalDateTime now = LocalDateTime.now();
        for (PmsSpecType specType : specTypes) {
            specType.setUpdateTime(now);
        }
        return this.updateBatchById(specTypes);
    }
}
