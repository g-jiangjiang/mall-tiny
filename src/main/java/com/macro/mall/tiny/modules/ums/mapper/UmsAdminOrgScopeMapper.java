package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdminOrgScope;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户组织权限范围表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2025-07-01
 */
public interface UmsAdminOrgScopeMapper extends BaseMapper<UmsAdminOrgScope> {

    /**
     * 根据用户ID获取权限范围列表
     */
    List<UmsAdminOrgScope> listByAdminId(@Param("adminId") Long adminId);

    /**
     * 根据用户ID删除权限范围
     */
    int deleteByAdminId(@Param("adminId") Long adminId);

}