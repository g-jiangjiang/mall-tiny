package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsOrgNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组织架构节点表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
public interface UmsOrgNodeMapper extends BaseMapper<UmsOrgNode> {

    /**
     * 根据层级获取节点列表
     */
    List<UmsOrgNode> selectByLevel(@Param("level") Integer level);

    /**
     * 根据父级ID获取子节点列表
     */
    List<UmsOrgNode> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 获取所有启用的节点
     */
    List<UmsOrgNode> selectAllEnabled();

}
