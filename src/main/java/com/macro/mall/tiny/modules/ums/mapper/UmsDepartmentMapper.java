package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsDepartment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsDepartmentMapper extends BaseMapper<UmsDepartment> {

    List<UmsDepartment> selectChildrenByParentId(@Param("parentId") Long parentId);

    List<Long> selectAllChildDeptIds(@Param("deptId") Long deptId);

    UmsDepartment selectByParentIdAndName(@Param("parentId") Long parentId, @Param("name") String name);
}
