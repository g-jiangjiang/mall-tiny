package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.dto.UmsDepartmentNode;
import com.macro.mall.tiny.modules.ums.dto.UmsDepartmentParam;
import com.macro.mall.tiny.modules.ums.mapper.UmsDepartmentMapper;
import com.macro.mall.tiny.modules.ums.model.UmsDepartment;
import com.macro.mall.tiny.modules.ums.service.UmsDepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmsDepartmentServiceImpl extends ServiceImpl<UmsDepartmentMapper, UmsDepartment> implements UmsDepartmentService {

    @Override
    public UmsDepartmentNode buildTree() {
        return buildTree(1L);
    }

    @Override
    public UmsDepartmentNode buildTree(Long rootId) {
        UmsDepartment root = getById(rootId);
        if (root == null) {
            return null;
        }
        UmsDepartmentNode rootNode = convertToNode(root);
        buildChildren(rootNode);
        return rootNode;
    }

    private void buildChildren(UmsDepartmentNode parentNode) {
        List<UmsDepartment> children = baseMapper.selectChildrenByParentId(parentNode.getId());
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        List<UmsDepartmentNode> childNodes = children.stream()
                .map(this::convertToNode)
                .collect(Collectors.toList());
        parentNode.setChildren(childNodes);
        for (UmsDepartmentNode childNode : childNodes) {
            buildChildren(childNode);
        }
    }

    private UmsDepartmentNode convertToNode(UmsDepartment dept) {
        UmsDepartmentNode node = new UmsDepartmentNode();
        BeanUtils.copyProperties(dept, node);
        return node;
    }

    @Override
    @Transactional
    public boolean create(UmsDepartmentParam param) {
        UmsDepartment parent = getById(param.getParentId());
        if (parent == null) {
            throw new RuntimeException("父部门不存在");
        }

        if (parent.getLevel() >= 5) {
            throw new RuntimeException("部门层级最多为5层");
        }

        UmsDepartment existDept = baseMapper.selectByParentIdAndName(param.getParentId(), param.getName());
        if (existDept != null) {
            throw new RuntimeException("同级部门名称已存在");
        }

        UmsDepartment dept = new UmsDepartment();
        BeanUtils.copyProperties(param, dept);
        dept.setLevel(parent.getLevel() + 1);
        dept.setStatus(param.getStatus() == null ? 1 : param.getStatus());
        dept.setSort(param.getSort() == null ? 0 : param.getSort());
        return save(dept);
    }

    @Override
    @Transactional
    public boolean update(UmsDepartmentParam param) {
        if (param.getId() == null) {
            throw new RuntimeException("部门ID不能为空");
        }

        UmsDepartment existDept = getById(param.getId());
        if (existDept == null) {
            throw new RuntimeException("部门不存在");
        }

        if (existDept.getId().equals(1L)) {
            throw new RuntimeException("全公司部门不允许修改");
        }

        UmsDepartment parent = getById(param.getParentId());
        if (parent == null) {
            throw new RuntimeException("父部门不存在");
        }

        if (parent.getLevel() >= 5) {
            throw new RuntimeException("部门层级最多为5层");
        }

        UmsDepartment sameNameDept = baseMapper.selectByParentIdAndName(param.getParentId(), param.getName());
        if (sameNameDept != null && !sameNameDept.getId().equals(param.getId())) {
            throw new RuntimeException("同级部门名称已存在");
        }

        UmsDepartment dept = new UmsDepartment();
        BeanUtils.copyProperties(param, dept);
        dept.setLevel(parent.getLevel() + 1);
        return updateById(dept);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (id == null) {
            throw new RuntimeException("部门ID不能为空");
        }

        UmsDepartment dept = getById(id);
        if (dept == null) {
            throw new RuntimeException("部门不存在");
        }

        if (dept.getId().equals(1L)) {
            throw new RuntimeException("全公司部门不允许删除");
        }

        List<UmsDepartment> children = listByParentId(id);
        if (!CollectionUtils.isEmpty(children)) {
            throw new RuntimeException("存在子部门，不允许删除");
        }

        return removeById(id);
    }

    @Override
    public List<Long> getAllChildDeptIds(Long deptId) {
        List<Long> childIds = new ArrayList<>();
        childIds.add(deptId);
        List<Long> directChildren = baseMapper.selectAllChildDeptIds(deptId);
        if (!CollectionUtils.isEmpty(directChildren)) {
            childIds.addAll(directChildren);
            for (Long childId : directChildren) {
                childIds.addAll(getAllChildDeptIds(childId));
            }
        }
        return childIds.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public UmsDepartment getById(Long id) {
        return super.getById(id);
    }

    @Override
    public List<UmsDepartment> listByParentId(Long parentId) {
        return baseMapper.selectChildrenByParentId(parentId);
    }
}
