package com.macro.mall.tiny.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.entity.PmsCategory;
import com.macro.mall.tiny.mapper.PmsCategoryMapper;
import com.macro.mall.tiny.service.PmsCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategory> implements PmsCategoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(PmsCategory category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setParentId(category.getParentId() == null ? 0L : category.getParentId());
        category.setLevel(category.getLevel() == null ? 0 : category.getLevel());
        return this.save(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long id, PmsCategory category) {
        category.setId(id);
        category.setUpdateTime(LocalDateTime.now());
        return this.updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public List<PmsCategory> listWithTree() {
        return this.list(new LambdaQueryWrapper<PmsCategory>()
                .orderByAsc(PmsCategory::getSort));
    }

    @Override
    public Page<PmsCategory> list(Long parentId, Integer pageNum, Integer pageSize) {
        Page<PmsCategory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsCategory> wrapper = new LambdaQueryWrapper<>();
        if (parentId != null) {
            wrapper.eq(PmsCategory::getParentId, parentId);
        }
        wrapper.orderByAsc(PmsCategory::getSort);
        return this.page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreate(List<PmsCategory> categories) {
        LocalDateTime now = LocalDateTime.now();
        for (PmsCategory category : categories) {
            category.setCreateTime(now);
            category.setUpdateTime(now);
        }
        return this.saveBatch(categories);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<PmsCategory> categories) {
        LocalDateTime now = LocalDateTime.now();
        for (PmsCategory category : categories) {
            category.setUpdateTime(now);
        }
        return this.updateBatchById(categories);
    }
}
