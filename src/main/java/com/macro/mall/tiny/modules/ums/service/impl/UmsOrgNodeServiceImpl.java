package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.dto.UmsOrgNodeDto;
import com.macro.mall.tiny.modules.ums.mapper.UmsOrgNodeMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleOrgMapper;
import com.macro.mall.tiny.modules.ums.model.UmsOrgNode;
import com.macro.mall.tiny.modules.ums.model.UmsRoleOrg;
import com.macro.mall.tiny.modules.ums.service.UmsOrgNodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织架构节点管理Service实现类
 * Created by macro on 2024/01/01.
 */
@Service
public class UmsOrgNodeServiceImpl extends ServiceImpl<UmsOrgNodeMapper, UmsOrgNode> implements UmsOrgNodeService {

    @Autowired
    private UmsRoleOrgMapper roleOrgMapper;

    @Override
    public boolean create(UmsOrgNode orgNode) {
        orgNode.setCreateTime(new Date());
        orgNode.setUpdateTime(new Date());
        orgNode.setStatus(1);
        return save(orgNode);
    }

    @Override
    public boolean update(Long id, UmsOrgNode orgNode) {
        orgNode.setId(id);
        orgNode.setUpdateTime(new Date());
        return updateById(orgNode);
    }

    @Override
    public boolean delete(Long id) {
        // 检查是否有子节点
        List<UmsOrgNode> children = baseMapper.selectByParentId(id);
        if (!CollectionUtils.isEmpty(children)) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public List<UmsOrgNodeDto> getOrgTree() {
        List<UmsOrgNode> allNodes = baseMapper.selectAllEnabled();
        return buildTree(allNodes, null, null);
    }

    @Override
    public List<UmsOrgNodeDto> getOrgTreeByRoleId(Long roleId) {
        // 获取所有组织架构节点
        List<UmsOrgNode> allNodes = baseMapper.selectAllEnabled();
        if (CollectionUtils.isEmpty(allNodes)) {
            return new ArrayList<>();
        }

        // 获取角色配置的组织架构ID
        List<UmsRoleOrg> roleOrgs = roleOrgMapper.selectByRoleId(roleId);
        Set<Long> roleOrgIds = roleOrgs.stream()
                .map(UmsRoleOrg::getOrgId)
                .collect(Collectors.toSet());

        // 获取角色配置类型和范围
        Integer configType = 0;
        Integer scopeType = 0;
        if (!CollectionUtils.isEmpty(roleOrgs)) {
            UmsRoleOrg firstRoleOrg = roleOrgs.get(0);
            configType = firstRoleOrg.getConfigType();
            scopeType = firstRoleOrg.getScopeType();
        }

        // 构建树形结构
        return buildTree(allNodes, roleOrgIds, scopeType);
    }

    /**
     * 构建树形结构
     */
    private List<UmsOrgNodeDto> buildTree(List<UmsOrgNode> nodes, Set<Long> roleOrgIds, Integer scopeType) {
        if (CollectionUtils.isEmpty(nodes)) {
            return new ArrayList<>();
        }

        // 转换为DTO
        List<UmsOrgNodeDto> dtoList = nodes.stream().map(node -> {
            UmsOrgNodeDto dto = new UmsOrgNodeDto();
            BeanUtils.copyProperties(node, dto);
            // 设置权限标记
            if (roleOrgIds != null) {
                dto.setHasPermission(roleOrgIds.contains(node.getId()));
            }
            if (scopeType != null) {
                dto.setScopeType(scopeType);
            }
            return dto;
        }).collect(Collectors.toList());

        // 按层级分组
        Map<Integer, List<UmsOrgNodeDto>> levelMap = dtoList.stream()
                .collect(Collectors.groupingBy(UmsOrgNodeDto::getLevel));

        // 构建树形结构
        List<UmsOrgNodeDto> result = new ArrayList<>();
        Map<Long, UmsOrgNodeDto> nodeMap = dtoList.stream()
                .collect(Collectors.toMap(UmsOrgNodeDto::getId, dto -> dto));

        for (UmsOrgNodeDto dto : dtoList) {
            if (dto.getParentId() == null || dto.getParentId() == 0) {
                // 根节点
                result.add(dto);
            } else {
                // 子节点
                UmsOrgNodeDto parent = nodeMap.get(dto.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(dto);
                }
            }
        }

        // 排序
        sortTree(result);
        return result;
    }

    /**
     * 递归排序树节点
     */
    private void sortTree(List<UmsOrgNodeDto> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }
        nodes.sort(Comparator.comparing(UmsOrgNodeDto::getSort, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(UmsOrgNodeDto::getId));
        for (UmsOrgNodeDto node : nodes) {
            if (!CollectionUtils.isEmpty(node.getChildren())) {
                sortTree(node.getChildren());
            }
        }
    }

    @Override
    public List<UmsOrgNode> listByLevel(Integer level) {
        return baseMapper.selectByLevel(level);
    }

    @Override
    public List<UmsOrgNode> listByParentId(Long parentId) {
        return baseMapper.selectByParentId(parentId);
    }

    @Override
    public boolean initDefaultOrg() {
        // 检查是否已初始化
        long count = count();
        if (count > 0) {
            return true;
        }

        Date now = new Date();

        // 第一层：全公司
        UmsOrgNode company = new UmsOrgNode();
        company.setName("全公司");
        company.setLevel(1);
        company.setParentId(0L);
        company.setSort(0);
        company.setStatus(1);
        company.setNodeType(0);
        company.setCreateTime(now);
        company.setUpdateTime(now);
        save(company);

        // 第二层：部门管理员（默认节点）
        UmsOrgNode deptAdmin = new UmsOrgNode();
        deptAdmin.setName("部门管理员");
        deptAdmin.setLevel(2);
        deptAdmin.setParentId(company.getId());
        deptAdmin.setSort(0);
        deptAdmin.setStatus(1);
        deptAdmin.setNodeType(1);
        deptAdmin.setCreateTime(now);
        deptAdmin.setUpdateTime(now);
        save(deptAdmin);

        // 第三层：部门（示例）
        UmsOrgNode dept1 = new UmsOrgNode();
        dept1.setName("技术部");
        dept1.setLevel(3);
        dept1.setParentId(deptAdmin.getId());
        dept1.setSort(0);
        dept1.setStatus(1);
        dept1.setNodeType(2);
        dept1.setCreateTime(now);
        dept1.setUpdateTime(now);
        save(dept1);

        UmsOrgNode dept2 = new UmsOrgNode();
        dept2.setName("市场部");
        dept2.setLevel(3);
        dept2.setParentId(deptAdmin.getId());
        dept2.setSort(1);
        dept2.setStatus(1);
        dept2.setNodeType(2);
        dept2.setCreateTime(now);
        dept2.setUpdateTime(now);
        save(dept2);

        // 第四层：小组（示例）
        UmsOrgNode group1 = new UmsOrgNode();
        group1.setName("前端组");
        group1.setLevel(4);
        group1.setParentId(dept1.getId());
        group1.setSort(0);
        group1.setStatus(1);
        group1.setNodeType(3);
        group1.setCreateTime(now);
        group1.setUpdateTime(now);
        save(group1);

        UmsOrgNode group2 = new UmsOrgNode();
        group2.setName("后端组");
        group2.setLevel(4);
        group2.setParentId(dept1.getId());
        group2.setSort(1);
        group2.setStatus(1);
        group2.setNodeType(3);
        group2.setCreateTime(now);
        group2.setUpdateTime(now);
        save(group2);

        // 第五层：岗位（示例）
        UmsOrgNode position1 = new UmsOrgNode();
        position1.setName("高级工程师");
        position1.setLevel(5);
        position1.setParentId(group1.getId());
        position1.setSort(0);
        position1.setStatus(1);
        position1.setNodeType(4);
        position1.setCreateTime(now);
        position1.setUpdateTime(now);
        save(position1);

        return true;
    }

}
