package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.model.PmsBrand;

import java.util.List;

/**
 * 商品品牌Service接口
 */
public interface PmsBrandService extends IService<PmsBrand> {

    /**
     * 分页查询品牌
     */
    Page<PmsBrand> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 获取所有品牌
     */
    List<PmsBrand> listAll();

    /**
     * 创建品牌
     */
    boolean create(PmsBrand brand);

    /**
     * 更新品牌
     */
    boolean update(Long id, PmsBrand brand);

    /**
     * 删除品牌
     */
    boolean delete(Long id);

    /**
     * 批量删除品牌
     */
    boolean deleteBatch(List<Long> ids);
}
