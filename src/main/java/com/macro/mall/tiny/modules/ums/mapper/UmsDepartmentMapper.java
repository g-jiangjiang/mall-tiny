package com.macro.mall.tiny.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.ums.model.UmsDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UmsDepartmentMapper extends BaseMapper<UmsDepartment> {

    List<UmsDepartment> getDepartmentListByRoleId(@Param("roleId") Long roleId);

    List<UmsDepartment> getDepartmentListByAdminId(@Param("adminId") Long adminId);
}
