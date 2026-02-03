package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.dto.UmsOrgNodeDto;
import com.macro.mall.tiny.modules.ums.model.UmsOrgNode;

import java.util.List;

/**
 * 组织架构节点管理Service
 * Created by macro on 2024/01/01.
 */
public interface UmsOrgNodeService extends IService<UmsOrgNode> {

    /**
     * 添加组织架构节点
     */
    boolean create(UmsOrgNode orgNode);

    /**
     * 修改组织架构节点
     */
    boolean update(Long id, UmsOrgNode orgNode);

    /**
     * 删除组织架构节点
     */
    boolean delete(Long id);

    /**
     * 获取组织架构树（完整树）
     */
    List<UmsOrgNodeDto> getOrgTree();

    /**
     * 根据角色ID获取组织架构树（带权限标记）
     */
    List<UmsOrgNodeDto> getOrgTreeByRoleId(Long roleId);

    /**
     * 获取指定层级的节点列表
     */
    List<UmsOrgNode> listByLevel(Integer level);

    /**
     * 获取子节点列表
     */
    List<UmsOrgNode> listByParentId(Long parentId);

    /**
     * 初始化默认组织架构（五层结构）
     * 第一层：全公司
     * 第二层：部门管理员
     * 第三层：部门
     * 第四层：小组
     * 第五层：岗位
     */
    boolean initDefaultOrg();

}
