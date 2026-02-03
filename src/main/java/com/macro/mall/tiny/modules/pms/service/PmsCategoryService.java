package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.dto.PmsCategoryNode;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;

import java.util.List;

/**
 * 商品类目Service接口
 */
public interface PmsCategoryService extends IService<PmsCategory> {

    /**
     * 创建类目
     */
    boolean create(PmsCategory category);

    /**
     * 更新类目
     */
    boolean update(Long id, PmsCategory category);

    /**
     * 删除类目
     */
    boolean delete(Long id);

    /**
     * 获取类目树
     */
    List<PmsCategoryNode> tree();

    /**
     * 根据父ID获取子类目
     */
    List<PmsCategory> listByParentId(Long parentId);

    /**
     * 获取类目详情
     */
    PmsCategory getById(Long id);
}
