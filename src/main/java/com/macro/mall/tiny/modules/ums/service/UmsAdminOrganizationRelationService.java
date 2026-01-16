package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.model.UmsAdminOrganizationRelation;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;

import java.util.List;

/**
 * 管理员组织关系管理Service
 *
 * @author macro
 * @since 2026-01-16
 */
public interface UmsAdminOrganizationRelationService extends IService<UmsAdminOrganizationRelation> {

    boolean updateOrganization(Long adminId, Long organizationId, Integer scope);

    UmsAdminOrganizationRelation getByAdminId(Long adminId);

    List<UmsOrganization> getManagedOrganizations(Long adminId);

}
