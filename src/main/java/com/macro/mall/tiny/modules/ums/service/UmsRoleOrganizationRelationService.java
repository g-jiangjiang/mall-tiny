package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.model.UmsRoleOrganizationRelation;

import java.util.List;

/**
 * <p>
 * 角色组织架构关联表 服务类
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
public interface UmsRoleOrganizationRelationService extends IService<UmsRoleOrganizationRelation> {

    /**
     * 根据角色ID获取组织架构ID列表
     * @param roleId 角色ID
     * @return 组织架构ID列表
     */
    List<Long> getOrganizationIdsByRoleId(Long roleId);

    /**
     * 根据管理员ID获取有权限的组织架构ID列表
     * @param adminId 管理员ID
     * @return 组织架构ID列表
     */
    List<Long> getOrganizationIdsByAdminId(Long adminId);

    /**
     * 为角色分配组织架构权限
     * @param roleId 角色ID
     * @param organizationIds 组织架构ID列表
     * @param scope 权限范围：0->仅当前组织；1->当前组织及下级组织
     * @return 分配数量
     */
    int allocOrganization(Long roleId, List<Long> organizationIds, Integer scope);

    /**
     * 删除角色的组织架构权限
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean removeByRoleId(Long roleId);

    /**
     * 删除组织架构的角色关联
     * @param organizationId 组织架构ID
     * @return 是否成功
     */
    boolean removeByOrganizationId(Long organizationId);
}