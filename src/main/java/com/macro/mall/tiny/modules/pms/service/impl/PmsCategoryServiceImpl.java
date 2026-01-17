package com.macro.mall.tiny.modules.pms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.dto.PmsCategoryParam;
import com.macro.mall.tiny.modules.pms.mapper.PmsCategoryMapper;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import com.macro.mall.tiny.modules.pms.service.PmsCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategory> implements PmsCategoryService {

    @Override
    public List<PmsCategory> listWithChildren() {
        List<PmsCategory> allCategories = list();
        return buildCategoryTree(allCategories, 0L);
    }

    @Override
    public boolean create(PmsCategoryParam categoryParam) {
        PmsCategory category = new PmsCategory();
        BeanUtils.copyProperties(categoryParam, category);
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        if (category.getLevel() == null) {
            category.setLevel(0);
        }
        return save(category);
    }

    @Override
    public boolean update(Long id, PmsCategoryParam categoryParam) {
        PmsCategory category = new PmsCategory();
        BeanUtils.copyProperties(categoryParam, category);
        category.setId(id);
        return updateById(category);
    }

    @Override
    public boolean delete(Long id) {
        LambdaQueryWrapper<PmsCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsCategory::getParentId, id);
        long count = count(wrapper);
        if (count > 0) {
            return false;
        }
        return removeById(id);
    }

    private List<PmsCategory> buildCategoryTree(List<PmsCategory> categories, Long parentId) {
        if (CollUtil.isEmpty(categories)) {
            return new ArrayList<>();
        }
        return categories.stream()
                .filter(category -> parentId.equals(category.getParentId()))
                .peek(category -> {
                    List<PmsCategory> children = buildCategoryTree(categories, category.getId());
                    category.setChildren(children);
                })
                .collect(Collectors.toList());
    }
}
