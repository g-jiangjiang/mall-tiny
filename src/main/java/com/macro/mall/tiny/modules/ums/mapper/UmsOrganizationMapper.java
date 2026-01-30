package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.dto.OrganizationTreeNode;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组织架构表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
public interface UmsOrganizationMapper extends BaseMapper<UmsOrganization> {

    /**
     * 获取组织架构树
     * @param adminId 管理员ID
     * @return 组织架构树节点列表
     */
    List<OrganizationTreeNode> getOrganizationTree(@Param("adminId") Long adminId);

    /**
     * 获取指定管理员有权限访问的组织ID列表
     * @param adminId 管理员ID
     * @return 组织ID列表
     */
    List<Long> getAccessibleOrganizationIds(@Param("adminId") Long adminId);

    /**
     * 获取指定组织及其所有下级组织ID
     * @param organizationId 组织ID
     * @return 组织ID列表
     */
    List<Long> getSubOrganizationIds(@Param("organizationId") Long organizationId);
    
    /**
     * 获取管理员的角色ID列表
     * @param adminId 管理员ID
     * @return 角色ID列表
     */
    List<Long> getAdminRoleIds(@Param("adminId") Long adminId);
}