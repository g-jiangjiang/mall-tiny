package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.dto.UmsDepartmentNode;
import com.macro.mall.tiny.modules.ums.model.UmsDepartment;

import java.util.List;

public interface UmsDepartmentService extends IService<UmsDepartment> {

    boolean create(UmsDepartment department);

    boolean update(Long id, UmsDepartment department);

    boolean delete(Long id);

    List<UmsDepartment> listByParentId(Long parentId);

    List<UmsDepartmentNode> treeList();

    List<UmsDepartmentNode> getDepartmentListByAdminId(Long adminId);

    List<UmsDepartmentNode> getConfigurableDepartmentTree(Long currentUserId);
}
