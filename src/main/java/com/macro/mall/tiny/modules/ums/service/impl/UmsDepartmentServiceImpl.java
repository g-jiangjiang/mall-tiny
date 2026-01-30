package com.macro.mall.tiny.modules.ums.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.dto.UmsDepartmentNode;
import com.macro.mall.tiny.modules.ums.mapper.UmsDepartmentMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelation;
import com.macro.mall.tiny.modules.ums.model.UmsDepartment;
import com.macro.mall.tiny.modules.ums.service.UmsAdminRoleRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsDepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmsDepartmentServiceImpl extends ServiceImpl<UmsDepartmentMapper, UmsDepartment> implements UmsDepartmentService {

    @Autowired
    private UmsAdminRoleRelationService adminRoleRelationService;

    private static final int MAX_LEVEL = 4;

    @Value("${mall-tiny.super-admin-role-ids:5}")
    private String superAdminRoleIds;

    @Override
    public boolean create(UmsDepartment department) {
        if (department.getParentId() == null || department.getParentId() == 0) {
            UmsDepartment root = getRootDepartment();
            if (root != null) {
                throw new RuntimeException("已存在顶级部门，不能创建新的顶级部门");
            }
            department.setLevel(0);
        } else {
            UmsDepartment parent = getById(department.getParentId());
            if (parent == null) {
                throw new RuntimeException("父级部门不存在");
            }
            if (parent.getLevel() >= MAX_LEVEL) {
                throw new RuntimeException("部门层级最多支持" + (MAX_LEVEL + 1) + "层");
            }
            department.setLevel(parent.getLevel() + 1);
        }
        department.setCreateTime(new Date());
        if (department.getSort() == null) {
            department.setSort(0);
        }
        if (department.getStatus() == null) {
            department.setStatus(1);
        }
        return save(department);
    }

    @Override
    public boolean update(Long id, UmsDepartment department) {
        department.setId(id);
        if (department.getParentId() != null && department.getParentId() != 0) {
            UmsDepartment parent = getById(department.getParentId());
            if (parent == null) {
                throw new RuntimeException("父级部门不存在");
            }
            if (parent.getLevel() >= MAX_LEVEL) {
                throw new RuntimeException("部门层级最多支持" + (MAX_LEVEL + 1) + "层");
            }
            department.setLevel(parent.getLevel() + 1);
        }
        return updateById(department);
    }

    @Override
    public boolean delete(Long id) {
        LambdaQueryWrapper<UmsDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsDepartment::getParentId, id);
        long childCount = count(wrapper);
        if (childCount > 0) {
            throw new RuntimeException("存在子部门，不能删除");
        }
        return removeById(id);
    }

    @Override
    public List<UmsDepartment> listByParentId(Long parentId) {
        LambdaQueryWrapper<UmsDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsDepartment::getParentId, parentId)
               .orderByAsc(UmsDepartment::getSort, UmsDepartment::getId);
        return list(wrapper);
    }

    @Override
    public List<UmsDepartmentNode> treeList() {
        List<UmsDepartment> allDepartments = list();
        return buildTree(allDepartments, 0L);
    }

    @Override
    public List<UmsDepartmentNode> getDepartmentListByAdminId(Long adminId) {
        if (isSuperAdmin(adminId)) {
            return treeList();
        }
        List<UmsDepartment> departments = baseMapper.getDepartmentListByAdminId(adminId);
        if (CollUtil.isEmpty(departments)) {
            return null;
        }
        return buildTree(departments, 0L);
    }

    @Override
    public List<UmsDepartmentNode> getConfigurableDepartmentTree(Long currentUserId) {
        if (isSuperAdmin(currentUserId)) {
            return treeList();
        }
        List<UmsDepartment> userDepartments = baseMapper.getDepartmentListByAdminId(currentUserId);
        if (CollUtil.isEmpty(userDepartments)) {
            return null;
        }
        return buildTree(userDepartments, 0L);
    }

    private UmsDepartment getRootDepartment() {
        LambdaQueryWrapper<UmsDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsDepartment::getParentId, 0L)
               .or()
               .eq(UmsDepartment::getLevel, 0);
        List<UmsDepartment> list = list(wrapper);
        return CollUtil.isEmpty(list) ? null : list.get(0);
    }

    private boolean isSuperAdmin(Long adminId) {
        List<UmsAdminRoleRelation> relations = adminRoleRelationService.list(
            new LambdaQueryWrapper<UmsAdminRoleRelation>()
                .eq(UmsAdminRoleRelation::getAdminId, adminId)
        );
        if (CollUtil.isEmpty(relations)) {
            return false;
        }
        List<Long> adminRoleIds = relations.stream()
            .map(UmsAdminRoleRelation::getRoleId)
            .collect(Collectors.toList());
        List<Long> superAdminIds = parseSuperAdminRoleIds();
        return adminRoleIds.stream()
            .anyMatch(superAdminIds::contains);
    }

    private List<Long> parseSuperAdminRoleIds() {
        if (superAdminRoleIds == null || superAdminRoleIds.trim().isEmpty()) {
            return List.of();
        }
        try {
            return java.util.Arrays.stream(superAdminRoleIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    private List<UmsDepartmentNode> buildTree(List<UmsDepartment> departments, Long parentId) {
        if (CollUtil.isEmpty(departments)) {
            return null;
        }
        List<UmsDepartmentNode> result = departments.stream()
            .filter(dept -> dept.getParentId() != null && dept.getParentId().equals(parentId))
            .map(dept -> {
                UmsDepartmentNode node = new UmsDepartmentNode();
                BeanUtils.copyProperties(dept, node);
                node.setChildren(buildTree(departments, dept.getId()));
                return node;
            })
            .collect(Collectors.toList());
        return CollUtil.isEmpty(result) ? null : result;
    }
}
