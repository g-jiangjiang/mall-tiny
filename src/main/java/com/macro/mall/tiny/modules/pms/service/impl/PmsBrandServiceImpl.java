package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.mapper.PmsBrandMapper;
import com.macro.mall.tiny.modules.pms.model.PmsBrand;
import com.macro.mall.tiny.modules.pms.service.PmsBrandService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandMapper, PmsBrand> implements PmsBrandService {

    @Override
    public Page<PmsBrand> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<PmsBrand> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsBrand> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(PmsBrand::getName, keyword);
        }
        wrapper.orderByDesc(PmsBrand::getSort);
        return page(page, wrapper);
    }

    @Override
    public boolean create(PmsBrand brand) {
        brand.setDeleteStatus(0);
        return save(brand);
    }

    @Override
    public boolean update(Long id, PmsBrand brand) {
        brand.setId(id);
        return updateById(brand);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }
}
