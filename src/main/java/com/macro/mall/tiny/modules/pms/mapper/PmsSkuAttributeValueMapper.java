package com.macro.mall.tiny.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.PmsSkuAttributeValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SKU规格属性值关联Mapper接口
 */
@Mapper
public interface PmsSkuAttributeValueMapper extends BaseMapper<PmsSkuAttributeValue> {

    /**
     * 根据SKU ID删除关联
     */
    int deleteBySkuId(@Param("skuId") Long skuId);

    /**
     * 批量插入
     */
    int batchInsert(@Param("list") List<PmsSkuAttributeValue> list);

    /**
     * 根据SKU ID列表删除关联
     */
    int deleteBySkuIds(@Param("skuIds") List<Long> skuIds);
}
