package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleOrganizationRelationMapper;
import com.macro.mall.tiny.modules.ums.model.UmsRoleOrganizationRelation;
import com.macro.mall.tiny.modules.ums.service.UmsRoleOrganizationRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色组织架构关联表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
@Service
public class UmsRoleOrganizationRelationServiceImpl extends ServiceImpl<UmsRoleOrganizationRelationMapper, UmsRoleOrganizationRelation> implements UmsRoleOrganizationRelationService {

    @Override
    public List<Long> getOrganizationIdsByRoleId(Long roleId) {
        return baseMapper.getOrganizationIdsByRoleId(roleId);
    }

    @Override
    public List<Long> getOrganizationIdsByAdminId(Long adminId) {
        return baseMapper.getOrganizationIdsByAdminId(adminId);
    }

    @Override
    public int allocOrganization(Long roleId, List<Long> organizationIds, Integer scope) {
        // 先删除原有关系
        LambdaQueryWrapper<UmsRoleOrganizationRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsRoleOrganizationRelation::getRoleId, roleId);
        remove(wrapper);
        
        // 批量插入新关系
        List<UmsRoleOrganizationRelation> relationList = new ArrayList<>();
        for (Long organizationId : organizationIds) {
            UmsRoleOrganizationRelation relation = new UmsRoleOrganizationRelation();
            relation.setRoleId(roleId);
            relation.setOrganizationId(organizationId);
            relation.setScope(scope);
            relation.setCreateTime(new Date());
            relationList.add(relation);
        }
        
        if (!relationList.isEmpty()) {
            // 使用自定义的insertBatch方法
            baseMapper.insertBatch(relationList);
        }
        
        return organizationIds.size();
    }

    @Override
    public boolean removeByRoleId(Long roleId) {
        LambdaQueryWrapper<UmsRoleOrganizationRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsRoleOrganizationRelation::getRoleId, roleId);
        return remove(wrapper);
    }

    @Override
    public boolean removeByOrganizationId(Long organizationId) {
        LambdaQueryWrapper<UmsRoleOrganizationRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsRoleOrganizationRelation::getOrganizationId, organizationId);
        return remove(wrapper);
    }
}