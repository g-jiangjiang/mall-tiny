package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.model.UmsRoleDepartmentRelation;

import java.util.List;

public interface UmsRoleDepartmentRelationService extends IService<UmsRoleDepartmentRelation> {

    boolean allocDepartments(Long roleId, List<Long> departmentIds);

    List<Long> getDepartmentIdsByRoleId(Long roleId);
}
