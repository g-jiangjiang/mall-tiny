package com.macro.mall.tiny.modules.ums.mapper;

import com.macro.mall.tiny.modules.ums.model.UmsOrganization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组织架构表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2026-01-16
 */
public interface UmsOrganizationMapper extends BaseMapper<UmsOrganization> {

    List<UmsOrganization> selectChildren(@Param("parentId") Long parentId);

    List<UmsOrganization> selectAllChildren(@Param("parentId") Long parentId);

}
