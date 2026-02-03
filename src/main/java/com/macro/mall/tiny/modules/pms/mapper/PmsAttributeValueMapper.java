package com.macro.mall.tiny.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.PmsAttributeValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品规格属性值Mapper接口
 */
@Mapper
public interface PmsAttributeValueMapper extends BaseMapper<PmsAttributeValue> {

    /**
     * 根据规格属性ID查询属性值列表
     */
    List<PmsAttributeValue> selectByAttributeId(@Param("attributeId") Long attributeId);
}
