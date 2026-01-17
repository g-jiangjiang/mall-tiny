package com.macro.mall.tiny.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.domain.PmsCategory;
import com.macro.mall.tiny.dto.PmsCategoryParam;
import com.macro.mall.tiny.mapper.PmsCategoryMapper;
import com.macro.mall.tiny.service.PmsCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategory> implements PmsCategoryService {

    @Override
    public Page<PmsCategory> list(Long parentId, Integer pageNum, Integer pageSize) {
        Page<PmsCategory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsCategory::getParentId, parentId)
                .orderByAsc(PmsCategory::getSort);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean create(PmsCategoryParam param) {
        PmsCategory category = new PmsCategory();
        BeanUtils.copyProperties(param, category);
        // 设置类目级别
        if (param.getParentId() == 0) {
            category.setLevel(1);
        } else {
            PmsCategory parentCategory = baseMapper.selectById(param.getParentId());
            if (parentCategory != null) {
                category.setLevel(parentCategory.getLevel() + 1);
            } else {
                return false;
            }
        }
        return save(category);
    }

    @Override
    public boolean update(Long id, PmsCategoryParam param) {
        PmsCategory category = new PmsCategory();
        BeanUtils.copyProperties(param, category);
        category.setId(id);
        return updateById(category);
    }

    @Override
    public boolean delete(Long id) {
        // 检查是否有子类目
        LambdaQueryWrapper<PmsCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsCategory::getParentId, id);
        long count = count(wrapper);
        if (count > 0) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public PmsCategory getDetail(Long id) {
        return getById(id);
    }

    @Override
    public List<PmsCategory> listAll() {
        LambdaQueryWrapper<PmsCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(PmsCategory::getSort);
        return list(wrapper);
    }
}
