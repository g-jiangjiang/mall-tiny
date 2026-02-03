package com.macro.mall.tiny.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品类目Mapper接口
 */
@Mapper
public interface PmsCategoryMapper extends BaseMapper<PmsCategory> {

    /**
     * 根据父ID查询子类目列表
     */
    List<PmsCategory> selectByParentId(@Param("parentId") Long parentId);
}
