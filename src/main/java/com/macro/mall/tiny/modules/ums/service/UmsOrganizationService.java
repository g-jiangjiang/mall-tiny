package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.dto.OrganizationTreeNode;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;

import java.util.List;

/**
 * <p>
 * 组织架构表 服务类
 * </p>
 *
 * @author macro
 * @since 2024-01-29
 */
public interface UmsOrganizationService extends IService<UmsOrganization> {

    /**
     * 获取组织架构树
     * @param adminId 管理员ID
     * @return 组织架构树节点列表
     */
    List<OrganizationTreeNode> getOrganizationTree(Long adminId);

    /**
     * 创建组织架构
     * @param organization 组织架构信息
     * @return 是否成功
     */
    boolean create(UmsOrganization organization);

    /**
     * 更新组织架构
     * @param id 组织架构ID
     * @param organization 组织架构信息
     * @return 是否成功
     */
    boolean update(Long id, UmsOrganization organization);

    /**
     * 删除组织架构
     * @param id 组织架构ID
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 获取组织架构详情
     * @param id 组织架构ID
     * @return 组织架构信息
     */
    UmsOrganization getItem(Long id);

    /**
     * 获取指定管理员有权限访问的组织ID列表
     * @param adminId 管理员ID
     * @return 组织ID列表
     */
    List<Long> getAccessibleOrganizationIds(Long adminId);

    /**
     * 初始化默认组织架构
     * 创建全公司根节点和默认部门
     */
    void initDefaultOrganization();
}