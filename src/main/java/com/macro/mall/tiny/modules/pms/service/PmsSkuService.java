package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.dto.PmsSkuBatchParam;
import com.macro.mall.tiny.modules.pms.model.PmsSku;

import java.util.List;

/**
 * 商品SKU Service接口
 */
public interface PmsSkuService extends IService<PmsSku> {

    /**
     * 根据SPU ID获取SKU列表
     */
    List<PmsSku> listBySpuId(Long spuId);

    /**
     * 批量创建SKU
     */
    boolean batchCreate(Long spuId, List<PmsSku> skuList);

    /**
     * 批量更新SKU
     */
    boolean batchUpdate(List<PmsSku> skuList);

    /**
     * 批量配置SKU（创建或更新）
     */
    boolean batchConfig(PmsSkuBatchParam batchParam);

    /**
     * 删除SKU
     */
    boolean delete(Long id);

    /**
     * 批量删除SKU
     */
    boolean deleteBatch(List<Long> ids);

    /**
     * 更新SKU库存
     */
    boolean updateStock(Long id, Integer stock);

    /**
     * 更新SKU价格
     */
    boolean updatePrice(Long id, java.math.BigDecimal price);

    /**
     * 获取SKU详情
     */
    PmsSku getDetail(Long id);
}
