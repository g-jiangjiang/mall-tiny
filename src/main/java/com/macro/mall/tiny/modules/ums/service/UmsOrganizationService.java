package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.model.UmsOrganization;

import java.util.List;

/**
 * 组织架构管理Service
 *
 * @author macro
 * @since 2026-01-16
 */
public interface UmsOrganizationService extends IService<UmsOrganization> {

    boolean create(UmsOrganization organization);

    boolean update(Long id, UmsOrganization organization);

    boolean delete(Long id);

    UmsOrganization get(Long id);

    List<UmsOrganization> listTree();

    List<UmsOrganization> listChildren(Long parentId);

    List<UmsOrganization> listAllChildren(Long parentId);

    Page<UmsOrganization> list(String keyword, Integer pageSize, Integer pageNum);

}
