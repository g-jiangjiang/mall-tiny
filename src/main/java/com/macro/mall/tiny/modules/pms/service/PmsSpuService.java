package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.dto.PmsSpuParam;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;

import java.util.List;

/**
 * 商品SPU Service接口
 */
public interface PmsSpuService extends IService<PmsSpu> {

    /**
     * 分页查询SPU
     */
    Page<PmsSpu> list(Long categoryId, Long brandId, String keyword, Integer pageSize, Integer pageNum);

    /**
     * 创建SPU（包含规格关联）
     */
    boolean create(PmsSpuParam spuParam);

    /**
     * 更新SPU
     */
    boolean update(Long id, PmsSpuParam spuParam);

    /**
     * 删除SPU
     */
    boolean delete(Long id);

    /**
     * 获取SPU详情（包含规格）
     */
    PmsSpuParam getDetail(Long id);

    /**
     * 批量上下架
     */
    boolean updateStatusBatch(List<Long> ids, Integer status);
}
