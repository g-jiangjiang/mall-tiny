package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsRoleOrganizationRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色组织架构关联表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
public interface UmsRoleOrganizationRelationMapper extends BaseMapper<UmsRoleOrganizationRelation> {

    /**
     * 根据角色ID获取组织架构ID列表
     * @param roleId 角色ID
     * @return 组织架构ID列表
     */
    List<Long> getOrganizationIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据管理员ID获取有权限的组织架构ID列表
     * @param adminId 管理员ID
     * @return 组织架构ID列表
     */
    List<Long> getOrganizationIdsByAdminId(@Param("adminId") Long adminId);

    /**
     * 批量插入角色组织架构关系
     * @param list 角色组织架构关系列表
     * @return 插入数量
     */
    int insertBatch(@Param("list") List<UmsRoleOrganizationRelation> list);
}