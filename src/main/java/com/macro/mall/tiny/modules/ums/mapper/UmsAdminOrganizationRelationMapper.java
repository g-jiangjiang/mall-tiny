package com.macro.mall.tiny.modules.ums.mapper;

import com.macro.mall.tiny.modules.ums.model.UmsAdminOrganizationRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 管理员组织关系表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2026-01-16
 */
public interface UmsAdminOrganizationRelationMapper extends BaseMapper<UmsAdminOrganizationRelation> {

    List<UmsAdminOrganizationRelation> selectByAdminId(@Param("adminId") Long adminId);

}
