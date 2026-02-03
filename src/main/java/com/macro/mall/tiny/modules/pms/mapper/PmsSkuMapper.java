package com.macro.mall.tiny.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.PmsSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品SKU Mapper接口
 */
@Mapper
public interface PmsSkuMapper extends BaseMapper<PmsSku> {

    /**
     * 根据SPU ID查询SKU列表
     */
    List<PmsSku> selectBySpuId(@Param("spuId") Long spuId);

    /**
     * 批量插入SKU
     */
    int batchInsert(@Param("list") List<PmsSku> list);

    /**
     * 批量更新SKU
     */
    int batchUpdate(@Param("list") List<PmsSku> list);

    /**
     * 根据SPU ID删除SKU
     */
    int deleteBySpuId(@Param("spuId") Long spuId);
}
