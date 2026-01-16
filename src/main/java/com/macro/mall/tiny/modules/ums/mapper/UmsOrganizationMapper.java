package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组织架构表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2025-07-01
 */
public interface UmsOrganizationMapper extends BaseMapper<UmsOrganization> {

    /**
     * 获取指定组织及其所有子组织ID
     */
    List<Long> getSubOrganizationIds(@Param("orgId") Long orgId);

    /**
     * 获取组织路径（从根到当前组织）
     */
    List<Long> getOrganizationPath(@Param("orgId") Long orgId);

    /**
     * 根据层级获取组织列表
     */
    List<UmsOrganization> listByLevel(@Param("level") Integer level);

}
