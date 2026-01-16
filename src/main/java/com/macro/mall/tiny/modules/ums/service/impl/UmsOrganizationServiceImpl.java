package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.mapper.UmsOrganizationMapper;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;
import com.macro.mall.tiny.modules.ums.service.UmsOrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 组织架构管理Service实现类
 *
 * @author macro
 * @since 2026-01-16
 */
@Service
public class UmsOrganizationServiceImpl extends ServiceImpl<UmsOrganizationMapper, UmsOrganization> implements UmsOrganizationService {

    @Override
    public boolean create(UmsOrganization organization) {
        if (organization.getParentId() == null || organization.getParentId() == 0) {
            organization.setParentId(0L);
            organization.setLevel(1);
        } else {
            UmsOrganization parent = getById(organization.getParentId());
            if (parent == null) {
                return false;
            }
            if (parent.getLevel() >= 5) {
                return false;
            }
            organization.setLevel(parent.getLevel() + 1);
        }
        organization.setCreateTime(new Date());
        if (organization.getSort() == null) {
            organization.setSort(0);
        }
        if (organization.getStatus() == null) {
            organization.setStatus(1);
        }
        return save(organization);
    }

    @Override
    public boolean update(Long id, UmsOrganization organization) {
        organization.setId(id);
        UmsOrganization existOrganization = getById(id);
        if (existOrganization == null) {
            return false;
        }
        if (organization.getParentId() != null && !organization.getParentId().equals(existOrganization.getParentId())) {
            if (organization.getParentId() == 0) {
                organization.setLevel(1);
            } else {
                UmsOrganization parent = getById(organization.getParentId());
                if (parent == null) {
                    return false;
                }
                if (parent.getLevel() >= 5) {
                    return false;
                }
                organization.setLevel(parent.getLevel() + 1);
            }
        }
        return updateById(organization);
    }

    @Override
    public boolean delete(Long id) {
        LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsOrganization::getParentId, id);
        List<UmsOrganization> children = list(wrapper);
        if (!children.isEmpty()) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public UmsOrganization get(Long id) {
        return getById(id);
    }

    @Override
    public List<UmsOrganization> listTree() {
        LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsOrganization::getStatus, 1)
               .orderByAsc(UmsOrganization::getLevel)
               .orderByAsc(UmsOrganization::getSort)
               .orderByAsc(UmsOrganization::getId);
        List<UmsOrganization> list = list(wrapper);
        return buildTree(list);
    }

    private List<UmsOrganization> buildTree(List<UmsOrganization> list) {
        List<UmsOrganization> tree = new ArrayList<>();
        for (UmsOrganization org : list) {
            if (org.getParentId() == 0) {
                org.setChildren(getChildren(org, list));
                tree.add(org);
            }
        }
        return tree;
    }

    private List<UmsOrganization> getChildren(UmsOrganization parent, List<UmsOrganization> list) {
        List<UmsOrganization> children = new ArrayList<>();
        for (UmsOrganization org : list) {
            if (parent.getId().equals(org.getParentId())) {
                org.setChildren(getChildren(org, list));
                children.add(org);
            }
        }
        return children;
    }

    @Override
    public List<UmsOrganization> listChildren(Long parentId) {
        return baseMapper.selectChildren(parentId);
    }

    @Override
    public List<UmsOrganization> listAllChildren(Long parentId) {
        return baseMapper.selectAllChildren(parentId);
    }

    @Override
    public Page<UmsOrganization> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<UmsOrganization> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(UmsOrganization::getName, keyword);
        }
        wrapper.orderByAsc(UmsOrganization::getLevel)
               .orderByAsc(UmsOrganization::getSort)
               .orderByAsc(UmsOrganization::getId);
        return page(page, wrapper);
    }

}
