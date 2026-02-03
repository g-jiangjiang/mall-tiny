package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.dto.PmsCategoryNode;
import com.macro.mall.tiny.modules.pms.mapper.PmsCategoryMapper;
import com.macro.mall.tiny.modules.pms.mapper.PmsSpuMapper;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;
import com.macro.mall.tiny.modules.pms.service.PmsCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品类目Service实现类
 */
@Service
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryMapper, PmsCategory> implements PmsCategoryService {

    @Autowired
    private PmsSpuMapper spuMapper;

    @Override
    public boolean create(PmsCategory category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        // 设置层级
        if (category.getParentId() == null || category.getParentId() == 0) {
            category.setParentId(0L);
            category.setLevel(1);
        } else {
            PmsCategory parent = getById(category.getParentId());
            if (parent != null) {
                category.setLevel(parent.getLevel() + 1);
            }
        }
        return save(category);
    }

    @Override
    public boolean update(Long id, PmsCategory category) {
        category.setId(id);
        category.setUpdateTime(LocalDateTime.now());
        return updateById(category);
    }

    @Override
    public boolean delete(Long id) {
        // 检查是否有子类目
        LambdaQueryWrapper<PmsCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsCategory::getParentId, id);
        long count = count(wrapper);
        if (count > 0) {
            throw new RuntimeException("该类目下存在子类目，无法删除");
        }

        // 检查是否被SPU使用
        List<PmsSpu> spuList = spuMapper.selectByCategoryId(id);
        if (!spuList.isEmpty()) {
            throw new RuntimeException("该类目已被商品使用，无法删除");
        }

        return removeById(id);
    }

    @Override
    public List<PmsCategoryNode> tree() {
        // 获取所有类目
        List<PmsCategory> allCategories = list();
        // 构建树形结构
        return buildTree(allCategories, 0L);
    }

    @Override
    public List<PmsCategory> listByParentId(Long parentId) {
        LambdaQueryWrapper<PmsCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsCategory::getParentId, parentId);
        wrapper.eq(PmsCategory::getStatus, 1);
        wrapper.orderByAsc(PmsCategory::getSort);
        return list(wrapper);
    }

    @Override
    public PmsCategory getById(Long id) {
        return super.getById(id);
    }

    /**
     * 递归构建类目树
     */
    private List<PmsCategoryNode> buildTree(List<PmsCategory> categories, Long parentId) {
        List<PmsCategoryNode> result = new ArrayList<>();
        for (PmsCategory category : categories) {
            if (category.getParentId().equals(parentId)) {
                PmsCategoryNode node = new PmsCategoryNode();
                BeanUtils.copyProperties(category, node);
                node.setChildren(buildTree(categories, category.getId()));
                result.add(node);
            }
        }
        // 排序
        return result.stream()
                .sorted((a, b) -> a.getSort() - b.getSort())
                .collect(Collectors.toList());
    }
}
