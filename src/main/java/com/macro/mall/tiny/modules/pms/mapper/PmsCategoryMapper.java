package com.macro.mall.tiny.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsCategoryMapper extends BaseMapper<PmsCategory> {

    List<PmsCategory> listWithChildren();
}
