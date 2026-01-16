package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.dto.UmsDepartmentNode;
import com.macro.mall.tiny.modules.ums.dto.UmsDepartmentParam;
import com.macro.mall.tiny.modules.ums.model.UmsDepartment;

import java.util.List;

public interface UmsDepartmentService extends IService<UmsDepartment> {

    UmsDepartmentNode buildTree();

    UmsDepartmentNode buildTree(Long rootId);

    boolean create(UmsDepartmentParam param);

    boolean update(UmsDepartmentParam param);

    boolean delete(Long id);

    List<Long> getAllChildDeptIds(Long deptId);

    UmsDepartment getById(Long id);

    List<UmsDepartment> listByParentId(Long parentId);
}
