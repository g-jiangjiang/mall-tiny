package com.macro.mall.tiny.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.PmsSpuAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SPU规格关联Mapper接口
 */
@Mapper
public interface PmsSpuAttributeMapper extends BaseMapper<PmsSpuAttribute> {

    /**
     * 根据SPU ID删除关联
     */
    int deleteBySpuId(@Param("spuId") Long spuId);

    /**
     * 批量插入
     */
    int batchInsert(@Param("list") List<PmsSpuAttribute> list);
}
