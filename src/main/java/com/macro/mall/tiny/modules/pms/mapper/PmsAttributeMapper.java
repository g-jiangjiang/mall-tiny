package com.macro.mall.tiny.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.PmsAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品规格属性Mapper接口
 */
@Mapper
public interface PmsAttributeMapper extends BaseMapper<PmsAttribute> {

    /**
     * 根据SPU ID查询规格列表
     */
    List<PmsAttribute> selectBySpuId(@Param("spuId") Long spuId);
}
