package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.domain.PmsCategory;
import com.macro.mall.tiny.modules.pms.mapper.PmsCategoryMapper;
import com.macro.mall.tiny.modules.pms.service.PmsCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategory> implements PmsCategoryService {

    @Override
    public List<PmsCategory> treeList() {
        List<PmsCategory> allCategories = list();
        return allCategories.stream()
                .filter(category -> category.getParentId() == null || category.getParentId() == 0)
                .peek(root -> root.setChildren(getChildren(root, allCategories)))
                .collect(Collectors.toList());
    }

    private List<PmsCategory> getChildren(PmsCategory parent, List<PmsCategory> allCategories) {
        return allCategories.stream()
                .filter(category -> parent.getId().equals(category.getParentId()))
                .peek(child -> child.setChildren(getChildren(child, allCategories)))
                .collect(Collectors.toList());
    }
}