package com.macro.mall.tiny.modules.ums.service;

import com.macro.mall.tiny.modules.ums.dto.RoleDataScopeParam;
import com.macro.mall.tiny.modules.ums.model.UmsRole;

import java.util.List;

public interface UmsRoleDataScopeService {

    boolean updateDataScope(RoleDataScopeParam param);

    List<Long> getDataScopeDeptIds(Long roleId);

    UmsRole getRoleWithDept(Long roleId);

    boolean allocDept(Long roleId, List<Long> deptIds);

    List<Long> getAllocatedDeptIds(Long roleId);
}
