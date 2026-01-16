package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.dto.RoleDataScopeParam;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleDeptRelationMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.model.UmsRoleDeptRelation;
import com.macro.mall.tiny.modules.ums.service.UmsDepartmentService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleDataScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UmsRoleDataScopeServiceImpl extends ServiceImpl<UmsRoleDeptRelationMapper, UmsRoleDeptRelation> implements UmsRoleDataScopeService {

    @Autowired
    private UmsRoleMapper roleMapper;

    @Autowired
    private UmsRoleDeptRelationMapper roleDeptRelationMapper;

    @Autowired
    private UmsDepartmentService departmentService;

    @Override
    @Transactional
    public boolean updateDataScope(RoleDataScopeParam param) {
        if (param.getRoleId() == null) {
            throw new RuntimeException("角色ID不能为空");
        }

        if (param.getDataScope() == null) {
            throw new RuntimeException("数据范围不能为空");
        }

        UmsRole role = roleMapper.selectById(param.getRoleId());
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        UmsRole updateRole = new UmsRole();
        updateRole.setId(param.getRoleId());
        updateRole.setDataScope(param.getDataScope());
        updateRole.setDeptId(param.getDeptId());

        if (param.getDataScope() == 2) {
            if (CollectionUtils.isEmpty(param.getDeptIds())) {
                throw new RuntimeException("自定义数据权限需要选择部门");
            }
            roleDeptRelationMapper.deleteByRoleId(param.getRoleId());
            return roleMapper.updateById(updateRole) > 0 && allocDept(param.getRoleId(), param.getDeptIds());
        } else {
            roleDeptRelationMapper.deleteByRoleId(param.getRoleId());
            return roleMapper.updateById(updateRole) > 0;
        }
    }

    @Override
    public List<Long> getDataScopeDeptIds(Long roleId) {
        UmsRole role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        Integer dataScope = role.getDataScope();
        if (dataScope == null) {
            dataScope = 1;
        }

        List<Long> deptIds = new ArrayList<>();

        switch (dataScope) {
            case 1:
                deptIds = null;
                break;
            case 2:
                deptIds = roleDeptRelationMapper.selectDeptIdsByRoleId(roleId);
                break;
            case 3:
                if (role.getDeptId() != null) {
                    deptIds.add(role.getDeptId());
                }
                break;
            case 4:
                if (role.getDeptId() != null) {
                    deptIds = departmentService.getAllChildDeptIds(role.getDeptId());
                }
                break;
            case 5:
                deptIds = new ArrayList<>();
                break;
            default:
                deptIds = null;
        }

        return deptIds;
    }

    @Override
    public UmsRole getRoleWithDept(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    @Override
    @Transactional
    public boolean allocDept(Long roleId, List<Long> deptIds) {
        if (roleId == null) {
            throw new RuntimeException("角色ID不能为空");
        }

        if (CollectionUtils.isEmpty(deptIds)) {
            return true;
        }

        roleDeptRelationMapper.deleteByRoleId(roleId);

        List<UmsRoleDeptRelation> relations = new ArrayList<>();
        for (Long deptId : deptIds) {
            UmsRoleDeptRelation relation = new UmsRoleDeptRelation();
            relation.setRoleId(roleId);
            relation.setDeptId(deptId);
            relations.add(relation);
        }

        return roleDeptRelationMapper.batchInsert(relations) > 0;
    }

    @Override
    public List<Long> getAllocatedDeptIds(Long roleId) {
        return roleDeptRelationMapper.selectDeptIdsByRoleId(roleId);
    }
}
