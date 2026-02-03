package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsRoleOrg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色组织架构关系表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
public interface UmsRoleOrgMapper extends BaseMapper<UmsRoleOrg> {

    /**
     * 根据角色ID获取组织架构配置
     */
    List<UmsRoleOrg> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入角色组织架构关系
     */
    int batchInsert(@Param("list") List<UmsRoleOrg> list);

    /**
     * 根据角色ID删除组织架构关系
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取角色配置的组织架构ID列表
     */
    List<Long> selectOrgIdsByRoleId(@Param("roleId") Long roleId);

}
