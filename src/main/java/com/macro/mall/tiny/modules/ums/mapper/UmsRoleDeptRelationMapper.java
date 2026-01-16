package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsRoleDeptRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsRoleDeptRelationMapper extends BaseMapper<UmsRoleDeptRelation> {

    int deleteByRoleId(@Param("roleId") Long roleId);

    int batchInsert(@Param("list") List<UmsRoleDeptRelation> list);

    List<Long> selectDeptIdsByRoleId(@Param("roleId") Long roleId);
}
