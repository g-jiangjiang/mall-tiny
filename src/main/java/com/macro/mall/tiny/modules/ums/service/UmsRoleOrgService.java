package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.dto.UmsRoleOrgDto;
import com.macro.mall.tiny.modules.ums.model.UmsRoleOrg;

import java.util.List;

/**
 * 角色组织架构配置Service
 * Created by macro on 2024/01/01.
 */
public interface UmsRoleOrgService extends IService<UmsRoleOrg> {

    /**
     * 获取角色的组织架构配置
     */
    UmsRoleOrgDto getRoleOrgConfig(Long roleId);

    /**
     * 配置角色组织架构
     * @param roleId 角色ID
     * @param scopeType 配置范围：0->本部门；1->全公司；2->指定部门
     * @param orgIds 指定部门ID列表（scopeType为2时使用）
     */
    boolean configRoleOrg(Long roleId, Integer scopeType, List<Long> orgIds);

    /**
     * 获取角色有权限的组织架构ID列表
     */
    List<Long> getRoleOrgIds(Long roleId);

    /**
     * 检查角色是否有指定组织架构的权限
     */
    boolean hasOrgPermission(Long roleId, Long orgId);

    /**
     * 批量配置角色组织架构（用于超管配置全公司范围）
     */
    boolean batchConfigRoleOrg(List<Long> roleIds, Integer scopeType);

    /**
     * 初始化角色的默认组织架构配置
     * 超管默认全公司范围，部门管理员默认本部门范围
     */
    boolean initDefaultConfig(Long roleId, Integer roleType);

}
