package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleDepartmentRelationMapper;
import com.macro.mall.tiny.modules.ums.model.UmsRoleDepartmentRelation;
import com.macro.mall.tiny.modules.ums.service.UmsRoleDepartmentRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmsRoleDepartmentRelationServiceImpl extends ServiceImpl<UmsRoleDepartmentRelationMapper, UmsRoleDepartmentRelation> implements UmsRoleDepartmentRelationService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean allocDepartments(Long roleId, List<Long> departmentIds) {
        LambdaQueryWrapper<UmsRoleDepartmentRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsRoleDepartmentRelation::getRoleId, roleId);
        remove(wrapper);
        if (departmentIds != null && !departmentIds.isEmpty()) {
            List<UmsRoleDepartmentRelation> relations = departmentIds.stream()
                .map(deptId -> new UmsRoleDepartmentRelation(roleId, deptId))
                .collect(Collectors.toList());
            return saveBatch(relations);
        }
        return true;
    }

    @Override
    public List<Long> getDepartmentIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<UmsRoleDepartmentRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsRoleDepartmentRelation::getRoleId, roleId);
        List<UmsRoleDepartmentRelation> relations = list(wrapper);
        return relations.stream()
            .map(UmsRoleDepartmentRelation::getDepartmentId)
            .collect(Collectors.toList());
    }
}
