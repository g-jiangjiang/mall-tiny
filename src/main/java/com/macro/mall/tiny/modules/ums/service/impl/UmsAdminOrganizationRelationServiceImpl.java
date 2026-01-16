package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminOrganizationRelationMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdminOrganizationRelation;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;
import com.macro.mall.tiny.modules.ums.service.UmsAdminOrganizationRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员组织关系管理Service实现类
 *
 * @author macro
 * @since 2026-01-16
 */
@Service
public class UmsAdminOrganizationRelationServiceImpl extends ServiceImpl<UmsAdminOrganizationRelationMapper, UmsAdminOrganizationRelation> implements UmsAdminOrganizationRelationService {

    @Autowired
    private UmsOrganizationService organizationService;

    @Override
    @Transactional
    public boolean updateOrganization(Long adminId, Long organizationId, Integer scope) {
        LambdaQueryWrapper<UmsAdminOrganizationRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsAdminOrganizationRelation::getAdminId, adminId);
        remove(wrapper);

        UmsAdminOrganizationRelation relation = new UmsAdminOrganizationRelation();
        relation.setAdminId(adminId);
        relation.setOrganizationId(organizationId);
        relation.setScope(scope);
        return save(relation);
    }

    @Override
    public UmsAdminOrganizationRelation getByAdminId(Long adminId) {
        LambdaQueryWrapper<UmsAdminOrganizationRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsAdminOrganizationRelation::getAdminId, adminId);
        return getOne(wrapper);
    }

    @Override
    public List<UmsOrganization> getManagedOrganizations(Long adminId) {
        UmsAdminOrganizationRelation relation = getByAdminId(adminId);
        if (relation == null) {
            return new ArrayList<>();
        }

        List<UmsOrganization> organizations = new ArrayList<>();
        UmsOrganization org = organizationService.get(relation.getOrganizationId());
        if (org != null) {
            if (relation.getScope() == 3) {
                LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(UmsOrganization::getStatus, 1);
                organizations = organizationService.list(wrapper);
            } else if (relation.getScope() == 2) {
                organizations.add(org);
                List<UmsOrganization> children = organizationService.listAllChildren(relation.getOrganizationId());
                organizations.addAll(children);
            } else {
                organizations.add(org);
            }
        }

        return organizations;
    }

}
