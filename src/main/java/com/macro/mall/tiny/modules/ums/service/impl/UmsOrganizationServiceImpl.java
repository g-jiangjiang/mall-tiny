package com.macro.mall.tiny.modules.ums.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.dto.OrganizationTreeNode;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminRoleRelationMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsOrganizationMapper;
import com.macro.mall.tiny.modules.ums.model.*;
import com.macro.mall.tiny.modules.ums.service.UmsOrganizationService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleOrganizationRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织架构表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
@Slf4j
@Service
public class UmsOrganizationServiceImpl extends ServiceImpl<UmsOrganizationMapper, UmsOrganization> implements UmsOrganizationService {

    @Autowired
    private UmsRoleOrganizationRelationService roleOrganizationRelationService;
    
    @Autowired
    private UmsRoleService roleService;
    
    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;

    @Override
    public List<OrganizationTreeNode> getOrganizationTree(Long adminId) {
        // 获取管理员有权限访问的组织ID列表
        List<Long> accessibleOrgIds = getAccessibleOrganizationIds(adminId);
        
        // 获取所有组织
        LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(UmsOrganization::getSort);
        List<UmsOrganization> allOrgs = list(wrapper);
        
        // 构建组织树
        Map<Long, OrganizationTreeNode> nodeMap = new HashMap<>();
        List<OrganizationTreeNode> rootNodes = new ArrayList<>();
        
        // 先创建所有节点
        for (UmsOrganization org : allOrgs) {
            OrganizationTreeNode node = new OrganizationTreeNode();
            node.setId(org.getId());
            node.setName(org.getName());
            node.setParentId(org.getParentId());
            node.setSort(org.getSort());
            node.setStatus(org.getStatus());
            node.setHasPermission(accessibleOrgIds.contains(org.getId()));
            nodeMap.put(org.getId(), node);
        }
        
        // 构建树形结构
        for (OrganizationTreeNode node : nodeMap.values()) {
            if (node.getParentId() == null || node.getParentId() == 0) {
                // 根节点
                rootNodes.add(node);
            } else {
                // 子节点
                OrganizationTreeNode parent = nodeMap.get(node.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(node);
                }
            }
        }
        
        return rootNodes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(UmsOrganization organization) {
        try {
            // 检查组织名称是否重复
            LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UmsOrganization::getName, organization.getName());
            if (count(wrapper) > 0) {
                log.warn("组织名称已存在: {}", organization.getName());
                return false;
            }
            
            // 设置默认值
            if (organization.getStatus() == null) {
                organization.setStatus(1);
            }
            if (organization.getSort() == null) {
                organization.setSort(0);
            }
            
            // 验证层级不超过5层
            if (organization.getParentId() != null && organization.getParentId() != 0) {
                UmsOrganization parent = getById(organization.getParentId());
                if (parent == null) {
                    log.warn("父组织不存在: {}", organization.getParentId());
                    return false;
                }
                organization.setLevel(parent.getLevel() + 1);
                
                // 检查层级是否超过5层
                if (organization.getLevel() > 5) {
                    log.warn("组织层级不能超过5层: {}", organization.getLevel());
                    return false;
                }
            } else {
                organization.setLevel(1);
            }
            
            organization.setCreateTime(new Date());
            organization.setUpdateTime(new Date());
            return save(organization);
        } catch (Exception e) {
            log.error("创建组织失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long id, UmsOrganization organization) {
        try {
            UmsOrganization existingOrg = getById(id);
            if (existingOrg == null) {
                log.warn("组织不存在: {}", id);
                return false;
            }
            
            // 检查名称是否重复（排除自己）
            if (!existingOrg.getName().equals(organization.getName())) {
                LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(UmsOrganization::getName, organization.getName())
                       .ne(UmsOrganization::getId, id);
                if (count(wrapper) > 0) {
                    log.warn("组织名称已存在: {}", organization.getName());
                    return false;
                }
            }
            
            // 如果修改了父组织，需要重新计算层级
            if (!Objects.equals(existingOrg.getParentId(), organization.getParentId())) {
                if (organization.getParentId() != null && organization.getParentId() > 0) {
                    UmsOrganization parent = getById(organization.getParentId());
                    if (parent == null) {
                        log.warn("父组织不存在: {}", organization.getParentId());
                        return false;
                    }
                    organization.setLevel(parent.getLevel() + 1);
                    
                    // 检查层级是否超过5层
                    if (organization.getLevel() > 5) {
                        log.warn("组织层级不能超过5层: {}", organization.getLevel());
                        return false;
                    }
                    
                    // 检查是否会造成循环引用
                    if (isCircularReference(id, organization.getParentId())) {
                        log.warn("组织架构不能循环引用: {} -> {}", id, organization.getParentId());
                        return false;
                    }
                } else {
                    organization.setLevel(1);
                }
            }
            
            organization.setId(id);
            organization.setUpdateTime(new Date());
            return updateById(organization);
        } catch (Exception e) {
            log.error("更新组织失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        try {
            UmsOrganization organization = getById(id);
            if (organization == null) {
                log.warn("组织不存在: {}", id);
                return false;
            }
            
            // 检查是否有子组织
            LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UmsOrganization::getParentId, id);
            long childCount = count(wrapper);
            if (childCount > 0) {
                log.warn("存在子组织，无法删除: {}", id);
                return false;
            }
            
            // 删除角色组织关联
            roleOrganizationRelationService.removeByOrganizationId(id);
            
            return removeById(id);
        } catch (Exception e) {
            log.error("删除组织失败", e);
            throw e;
        }
    }

    @Override
    public UmsOrganization getItem(Long id) {
        return getById(id);
    }

    @Override
    public List<Long> getAccessibleOrganizationIds(Long adminId) {
        // 获取管理员的角色
        List<Long> roleIds = adminRoleRelationMapper.getAdminRoleIds(adminId);
        
        // 检查是否是超级管理员
        boolean isSuperAdmin = false;
        for (Long roleId : roleIds) {
            UmsRole role = roleService.getById(roleId);
            if (role != null && "超级管理员".equals(role.getName())) {
                isSuperAdmin = true;
                break;
            }
        }
        
        if (isSuperAdmin) {
            // 超级管理员可以访问所有组织
            LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(UmsOrganization::getId);
            wrapper.eq(UmsOrganization::getStatus, 1);
            return listObjs(wrapper, obj -> (Long) obj);
        }
        
        // 获取角色有权限的组织ID列表
        List<Long> directOrgIds = new ArrayList<>();
        List<Long> includeSubOrgIds = new ArrayList<>();
        
        for (Long roleId : roleIds) {
            List<UmsRoleOrganizationRelation> relations = roleOrganizationRelationService
                    .lambdaQuery()
                    .eq(UmsRoleOrganizationRelation::getRoleId, roleId)
                    .list();
            
            for (UmsRoleOrganizationRelation relation : relations) {
                if (relation.getScope() == 0) {
                    // 仅当前组织
                    directOrgIds.add(relation.getOrganizationId());
                } else {
                    // 当前组织及下级组织
                    includeSubOrgIds.add(relation.getOrganizationId());
                }
            }
        }
        
        // 获取包含下级的组织ID列表
        List<Long> subOrgIds = new ArrayList<>();
        for (Long orgId : includeSubOrgIds) {
            List<Long> ids = baseMapper.getSubOrganizationIds(orgId);
            subOrgIds.addAll(ids);
        }
        
        // 合并所有可访问的组织ID
        Set<Long> allOrgIds = new HashSet<>();
        allOrgIds.addAll(directOrgIds);
        allOrgIds.addAll(subOrgIds);
        
        return new ArrayList<>(allOrgIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initDefaultOrganization() {
        try {
            // 检查是否已存在组织架构
            LambdaQueryWrapper<UmsOrganization> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UmsOrganization::getCode, "COMPANY_ROOT");
            long count = count(wrapper);
            if (count > 0) {
                log.info("组织架构已初始化，跳过");
                return; // 已初始化
            }
            
            // 创建全公司根节点
            UmsOrganization company = new UmsOrganization();
            company.setName("全公司");
            company.setCode("COMPANY_ROOT");
            company.setParentId(0L);
            company.setLevel(1);
            company.setSort(0);
            company.setStatus(1);
            company.setDescription("公司根节点");
            company.setCreateTime(new Date());
            company.setUpdateTime(new Date());
            save(company);
            
            // 创建默认部门
            UmsOrganization defaultDept = new UmsOrganization();
            defaultDept.setName("默认部门");
            defaultDept.setCode("DEFAULT_DEPT");
            defaultDept.setParentId(company.getId());
            defaultDept.setLevel(2);
            defaultDept.setSort(0);
            defaultDept.setStatus(1);
            defaultDept.setDescription("默认部门");
            defaultDept.setCreateTime(new Date());
            defaultDept.setUpdateTime(new Date());
            save(defaultDept);
            
            log.info("组织架构初始化完成");
        } catch (Exception e) {
            log.error("组织架构初始化失败", e);
            throw e;
        }
    }

    /**
     * 检查是否会造成循环引用
     * @param orgId 组织ID
     * @param parentId 父组织ID
     * @return 是否会造成循环引用
     */
    private boolean isCircularReference(Long orgId, Long parentId) {
        if (parentId == null || parentId == 0) {
            return false;
        }
        
        // 如果父组织ID等于当前组织ID，会造成循环引用
        if (orgId.equals(parentId)) {
            return true;
        }
        
        // 递归检查父组织的父组织
        UmsOrganization parent = getById(parentId);
        if (parent == null) {
            return false;
        }
        
        return isCircularReference(orgId, parent.getParentId());
    }

    private OrganizationTreeNode convertToTreeNode(UmsOrganization org) {
        OrganizationTreeNode node = new OrganizationTreeNode();
        node.setId(org.getId());
        node.setName(org.getName());
        node.setCode(org.getCode());
        node.setParentId(org.getParentId());
        node.setLevel(org.getLevel());
        node.setSort(org.getSort());
        node.setStatus(org.getStatus());
        node.setDescription(org.getDescription());
        return node;
    }

    private void sortTreeNodes(List<OrganizationTreeNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        
        // 按sort字段排序
        nodes.sort(Comparator.comparing(OrganizationTreeNode::getSort, Comparator.nullsLast(Integer::compareTo)));
        
        // 递归排序子节点
        for (OrganizationTreeNode node : nodes) {
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                sortTreeNodes(node.getChildren());
            }
        }
    }
}