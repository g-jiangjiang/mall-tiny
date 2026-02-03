package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.dto.UmsOrgNodeDto;
import com.macro.mall.tiny.modules.ums.dto.UmsRoleOrgDto;
import com.macro.mall.tiny.modules.ums.mapper.UmsOrgNodeMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleOrgMapper;
import com.macro.mall.tiny.modules.ums.model.UmsOrgNode;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.model.UmsRoleOrg;
import com.macro.mall.tiny.modules.ums.service.UmsOrgNodeService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleOrgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色组织架构配置Service实现类
 * Created by macro on 2024/01/01.
 */
@Service
public class UmsRoleOrgServiceImpl extends ServiceImpl<UmsRoleOrgMapper, UmsRoleOrg> implements UmsRoleOrgService {

    @Autowired
    private UmsRoleOrgMapper roleOrgMapper;

    @Autowired
    private UmsOrgNodeMapper orgNodeMapper;

    @Autowired
    private UmsRoleMapper roleMapper;

    @Autowired
    private UmsOrgNodeService orgNodeService;

    // 角色类型常量
    private static final String SUPER_ADMIN_NAME = "超级管理员";
    private static final String DEPT_ADMIN_NAME = "部门管理员";

    // 配置范围常量
    private static final Integer SCOPE_SELF_DEPT = 0;  // 本部门
    private static final Integer SCOPE_ALL_COMPANY = 1; // 全公司
    private static final Integer SCOPE_SPECIFIED = 2;   // 指定部门

    // 配置类型常量
    private static final Integer CONFIG_TYPE_DEFAULT = 0;  // 默认配置
    private static final Integer CONFIG_TYPE_CUSTOM = 1;   // 自定义配置

    @Override
    public UmsRoleOrgDto getRoleOrgConfig(Long roleId) {
        UmsRoleOrgDto dto = new UmsRoleOrgDto();
        dto.setRoleId(roleId);

        // 获取角色信息
        UmsRole role = roleMapper.selectById(roleId);
        if (role != null) {
            dto.setRoleName(role.getName());
        }

        // 获取组织架构树
        List<UmsOrgNodeDto> orgTree = orgNodeService.getOrgTreeByRoleId(roleId);
        dto.setOrgTree(orgTree);

        // 获取角色配置
        List<UmsRoleOrg> roleOrgs = roleOrgMapper.selectByRoleId(roleId);
        if (!CollectionUtils.isEmpty(roleOrgs)) {
            UmsRoleOrg firstRoleOrg = roleOrgs.get(0);
            dto.setConfigType(firstRoleOrg.getConfigType());
            dto.setScopeType(firstRoleOrg.getScopeType());
            dto.setSelectedOrgIds(roleOrgs.stream()
                    .map(UmsRoleOrg::getOrgId)
                    .collect(Collectors.toList()));
        } else {
            // 没有配置时使用默认配置
            dto.setConfigType(CONFIG_TYPE_DEFAULT);
            if (role != null && SUPER_ADMIN_NAME.equals(role.getName())) {
                dto.setScopeType(SCOPE_ALL_COMPANY);
            } else {
                dto.setScopeType(SCOPE_SELF_DEPT);
            }
            dto.setSelectedOrgIds(new ArrayList<>());
        }

        return dto;
    }

    @Override
    @Transactional
    public boolean configRoleOrg(Long roleId, Integer scopeType, List<Long> orgIds) {
        // 删除原有配置
        roleOrgMapper.deleteByRoleId(roleId);

        Date now = new Date();
        List<UmsRoleOrg> roleOrgList = new ArrayList<>();

        // 根据范围类型处理
        if (SCOPE_ALL_COMPANY.equals(scopeType)) {
            // 全公司范围：关联第一层节点（全公司）
            List<UmsOrgNode> level1Nodes = orgNodeMapper.selectByLevel(1);
            for (UmsOrgNode node : level1Nodes) {
                UmsRoleOrg roleOrg = createRoleOrg(roleId, node, CONFIG_TYPE_CUSTOM, scopeType, now);
                roleOrgList.add(roleOrg);
            }
        } else if (SCOPE_SELF_DEPT.equals(scopeType)) {
            // 本部门范围：关联第二层节点（部门管理员）
            List<UmsOrgNode> level2Nodes = orgNodeMapper.selectByLevel(2);
            for (UmsOrgNode node : level2Nodes) {
                UmsRoleOrg roleOrg = createRoleOrg(roleId, node, CONFIG_TYPE_CUSTOM, scopeType, now);
                roleOrgList.add(roleOrg);
            }
        } else if (SCOPE_SPECIFIED.equals(scopeType) && !CollectionUtils.isEmpty(orgIds)) {
            // 指定部门范围：关联指定的节点
            List<UmsOrgNode> nodes = orgNodeMapper.selectBatchIds(orgIds);
            for (UmsOrgNode node : nodes) {
                UmsRoleOrg roleOrg = createRoleOrg(roleId, node, CONFIG_TYPE_CUSTOM, scopeType, now);
                roleOrgList.add(roleOrg);
            }
        }

        // 批量插入
        if (!CollectionUtils.isEmpty(roleOrgList)) {
            roleOrgMapper.batchInsert(roleOrgList);
        }

        return true;
    }

    /**
     * 创建角色组织架构关系对象
     */
    private UmsRoleOrg createRoleOrg(Long roleId, UmsOrgNode node, Integer configType, Integer scopeType, Date now) {
        UmsRoleOrg roleOrg = new UmsRoleOrg();
        roleOrg.setRoleId(roleId);
        roleOrg.setOrgId(node.getId());
        roleOrg.setOrgName(node.getName());
        roleOrg.setOrgLevel(node.getLevel());
        roleOrg.setParentId(node.getParentId());
        roleOrg.setConfigType(configType);
        roleOrg.setScopeType(scopeType);
        roleOrg.setCreateTime(now);
        roleOrg.setUpdateTime(now);
        return roleOrg;
    }

    @Override
    public List<Long> getRoleOrgIds(Long roleId) {
        return roleOrgMapper.selectOrgIdsByRoleId(roleId);
    }

    @Override
    public boolean hasOrgPermission(Long roleId, Long orgId) {
        List<Long> orgIds = getRoleOrgIds(roleId);
        if (CollectionUtils.isEmpty(orgIds)) {
            return false;
        }

        // 检查是否有权限（包括父级权限）
        Set<Long> allPermittedIds = new HashSet<>(orgIds);

        // 获取所有子节点ID
        for (Long permittedId : orgIds) {
            List<UmsOrgNode> children = getAllChildren(permittedId);
            for (UmsOrgNode child : children) {
                allPermittedIds.add(child.getId());
            }
        }

        return allPermittedIds.contains(orgId);
    }

    /**
     * 递归获取所有子节点
     */
    private List<UmsOrgNode> getAllChildren(Long parentId) {
        List<UmsOrgNode> result = new ArrayList<>();
        List<UmsOrgNode> children = orgNodeMapper.selectByParentId(parentId);
        result.addAll(children);
        for (UmsOrgNode child : children) {
            result.addAll(getAllChildren(child.getId()));
        }
        return result;
    }

    @Override
    @Transactional
    public boolean batchConfigRoleOrg(List<Long> roleIds, Integer scopeType) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return true;
        }

        for (Long roleId : roleIds) {
            configRoleOrg(roleId, scopeType, null);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean initDefaultConfig(Long roleId, Integer roleType) {
        // 先检查是否已有配置
        List<UmsRoleOrg> existing = roleOrgMapper.selectByRoleId(roleId);
        if (!CollectionUtils.isEmpty(existing)) {
            return true;
        }

        // 获取角色信息
        UmsRole role = roleMapper.selectById(roleId);
        if (role == null) {
            return false;
        }

        Date now = new Date();
        Integer scopeType;
        List<UmsOrgNode> targetNodes;

        // 根据角色名称判断类型
        if (SUPER_ADMIN_NAME.equals(role.getName())) {
            // 超管默认全公司范围
            scopeType = SCOPE_ALL_COMPANY;
            targetNodes = orgNodeMapper.selectByLevel(1);
        } else if (DEPT_ADMIN_NAME.equals(role.getName())) {
            // 部门管理员默认本部门范围
            scopeType = SCOPE_SELF_DEPT;
            targetNodes = orgNodeMapper.selectByLevel(2);
        } else {
            // 其他角色默认本部门范围
            scopeType = SCOPE_SELF_DEPT;
            targetNodes = orgNodeMapper.selectByLevel(2);
        }

        // 创建默认配置
        List<UmsRoleOrg> roleOrgList = new ArrayList<>();
        for (UmsOrgNode node : targetNodes) {
            UmsRoleOrg roleOrg = createRoleOrg(roleId, node, CONFIG_TYPE_DEFAULT, scopeType, now);
            roleOrgList.add(roleOrg);
        }

        if (!CollectionUtils.isEmpty(roleOrgList)) {
            roleOrgMapper.batchInsert(roleOrgList);
        }

        return true;
    }

}
