package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;

import java.util.List;

/**
 * <p>
 * 组织架构表 Service 接口
 * </p>
 *
 * @author macro
 * @since 2025-07-01
 */
public interface UmsOrganizationService extends IService<UmsOrganization> {

    /**
     * 获取组织架构树
     */
    List<UmsOrganization> treeList();

    /**
     * 根据层级获取组织列表
     */
    List<UmsOrganization> listByLevel(Integer level);

    /**
     * 获取指定组织及其所有子组织ID
     */
    List<Long> getSubOrganizationIds(Long orgId);

    /**
     * 获取组织路径（从根到当前组织）
     */
    List<Long> getOrganizationPath(Long orgId);

    /**
     * 检查创建组织是否超过层级限制
     */
    boolean checkLevelLimit(Long parentId);

    /**
     * 获取默认组织
     */
    UmsOrganization getDefaultOrganization();

}
