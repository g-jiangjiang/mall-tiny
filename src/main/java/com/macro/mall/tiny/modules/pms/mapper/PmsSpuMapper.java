package com.macro.mall.tiny.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;
import org.apache.ibatis.annotations.Param;

public interface PmsSpuMapper extends BaseMapper<PmsSpu> {

    Page<PmsSpu> selectPageWithBrandAndCategory(Page<PmsSpu> page, @Param("keyword") String keyword);
}
