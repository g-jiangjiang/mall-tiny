package com.macro.mall.tiny.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品SPU Mapper接口
 */
@Mapper
public interface PmsSpuMapper extends BaseMapper<PmsSpu> {

    /**
     * 根据类目ID查询SPU列表
     */
    List<PmsSpu> selectByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据品牌ID查询SPU列表
     */
    List<PmsSpu> selectByBrandId(@Param("brandId") Long brandId);
}
