package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.mapper.UmsOrganizationMapper;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;
import com.macro.mall.tiny.modules.ums.service.UmsOrganizationService;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织架构表 Service 实现类
 * </p>
 *
 * @author macro
 * @since 2025-07-01
 */
@Service
public class UmsOrganizationServiceImpl extends ServiceImpl<UmsOrganizationMapper, UmsOrganization> implements UmsOrganizationService {

    private static final int MAX_LEVEL = 5;

    @Override
    public List<UmsOrganization> treeList() {
        // 获取所有启用的组织
        LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsOrganization::getStatus, 1).orderByAsc(UmsOrganization::getSort, UmsOrganization::getId);
        List<UmsOrganization> allOrganizations = list(wrapper);

        // 按父级ID分组
        Map<Long, List<UmsOrganization>> groupByParentId = allOrganizations.stream()
                .collect(Collectors.groupingBy(UmsOrganization::getParentId));

        // 构建树结构
        return buildTree(null, groupByParentId);
    }

    private List<UmsOrganization> buildTree(Long parentId, Map<Long, List<UmsOrganization>> groupByParentId) {
        List<UmsOrganization> children = groupByParentId.get(parentId);
        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }
        for (UmsOrganization child : children) {
            child.setChildren(buildTree(child.getId(), groupByParentId));
        }
        return children;
    }

    @Override
    public List<UmsOrganization> listByLevel(Integer level) {
        return baseMapper.listByLevel(level);
    }

    @Override
    public List<Long> getSubOrganizationIds(Long orgId) {
        if (orgId == null) {
            return Collections.emptyList();
        }
        return baseMapper.getSubOrganizationIds(orgId);
    }

    @Override
    public List<Long> getOrganizationPath(Long orgId) {
        if (orgId == null) {
            return Collections.emptyList();
        }
        return baseMapper.getOrganizationPath(orgId);
    }

    @Override
    public boolean checkLevelLimit(Long parentId) {
        if (parentId == null) {
            // 顶级组织，层级为1
            return true;
        }
        UmsOrganization parent = getById(parentId);
        if (parent == null) {
            return false;
        }
        // 检查是否超过最大层级
        return parent.getLevel() < MAX_LEVEL;
    }

    @Override
    public UmsOrganization getDefaultOrganization() {
        LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsOrganization::getIsDefault, 1)
                .eq(UmsOrganization::getStatus, 1)
                .orderByDesc(UmsOrganization::getId)
                .last("LIMIT 1");
        return getOne(wrapper);
    }

}
