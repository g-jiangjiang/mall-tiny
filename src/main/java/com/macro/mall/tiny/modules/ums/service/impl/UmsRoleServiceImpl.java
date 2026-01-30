package com.macro.mall.tiny.modules.ums.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.mapper.UmsMenuMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsOrganizationMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper;
import com.macro.mall.tiny.modules.ums.model.*;
import com.macro.mall.tiny.modules.ums.service.UmsAdminCacheService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleMenuRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleOrganizationRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleResourceRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台角色管理Service实现类
 * Created by macro on 2018/9/30.
 */
@Service
public class UmsRoleServiceImpl extends ServiceImpl<UmsRoleMapper,UmsRole>implements UmsRoleService {
    @Autowired
    private UmsAdminCacheService adminCacheService;
    @Autowired
    private UmsRoleMenuRelationService roleMenuRelationService;
    @Autowired
    private UmsRoleResourceRelationService roleResourceRelationService;
    @Autowired
    private UmsRoleOrganizationRelationService roleOrganizationRelationService;
    
    @Autowired
    private UmsOrganizationMapper organizationMapper;
    @Autowired
    private UmsMenuMapper menuMapper;
    @Autowired
    private UmsResourceMapper resourceMapper;
    
    @Override
    public boolean create(UmsRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        return save(role);
    }

    @Override
    public boolean delete(List<Long> ids) {
        boolean success = removeByIds(ids);
        adminCacheService.delResourceListByRoleIds(ids);
        
        // 删除角色相关的组织架构权限
        for (Long roleId : ids) {
            roleOrganizationRelationService.removeByRoleId(roleId);
        }
        
        return success;
    }

    @Override
    public Page<UmsRole> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<UmsRole> page = new Page<>(pageNum,pageSize);
        QueryWrapper<UmsRole> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<UmsRole> lambda = wrapper.lambda();
        if(StrUtil.isNotEmpty(keyword)){
            lambda.like(UmsRole::getName,keyword);
        }
        return page(page,wrapper);
    }

    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return menuMapper.getMenuList(adminId);
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
        return menuMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public List<UmsResource> listResource(Long roleId) {
        return resourceMapper.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        QueryWrapper<UmsRoleMenuRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsRoleMenuRelation::getRoleId,roleId);
        roleMenuRelationService.remove(wrapper);
        //批量插入新关系
        List<UmsRoleMenuRelation> relationList = new ArrayList<>();
        for (Long menuId : menuIds) {
            UmsRoleMenuRelation relation = new UmsRoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            relationList.add(relation);
        }
        roleMenuRelationService.saveBatch(relationList);
        return menuIds.size();
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        //先删除原有关系
        QueryWrapper<UmsRoleResourceRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsRoleResourceRelation::getRoleId,roleId);
        roleResourceRelationService.remove(wrapper);
        //批量插入新关系
        List<UmsRoleResourceRelation> relationList = new ArrayList<>();
        for (Long resourceId : resourceIds) {
            UmsRoleResourceRelation relation = new UmsRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            relationList.add(relation);
        }
        roleResourceRelationService.saveBatch(relationList);
        adminCacheService.delResourceListByRole(roleId);
        return resourceIds.size();
    }

    @Override
    public boolean hasOrganizationPermission(Long roleId, Long organizationId) {
        // 检查是否是超级管理员
        UmsRole role = getById(roleId);
        if (role != null && "超级管理员".equals(role.getName())) {
            return true;
        }
        
        // 检查角色是否有该组织架构的权限
        List<UmsRoleOrganizationRelation> relations = roleOrganizationRelationService
                .lambdaQuery()
                .eq(UmsRoleOrganizationRelation::getRoleId, roleId)
                .list();
        
        for (UmsRoleOrganizationRelation relation : relations) {
            if (relation.getOrganizationId().equals(organizationId)) {
                return true;
            }
            
            // 如果scope为1（包含下级组织），检查该组织是否是权限组织的下级
            if (relation.getScope() == 1) {
                List<Long> subOrgIds = organizationMapper.getSubOrganizationIds(relation.getOrganizationId());
                if (subOrgIds.contains(organizationId)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public List<Long> getOrganizationPermissionScope(Long roleId) {
        // 超级管理员可以访问所有组织
        UmsRole role = getById(roleId);
        if (role != null && "超级管理员".equals(role.getName())) {
            // 返回所有组织ID的逻辑需要在OrganizationService中实现
            // 这里暂时返回空列表，实际使用时需要实现
            return new ArrayList<>();
        }
        
        // 获取角色直接关联的组织ID
        return roleOrganizationRelationService.getOrganizationIdsByRoleId(roleId);
    }
}
