package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.mapper.PmsBrandMapper;
import com.macro.mall.tiny.modules.pms.model.PmsBrand;
import com.macro.mall.tiny.modules.pms.service.PmsBrandService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品品牌Service实现类
 */
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
    public List<PmsBrand> listAll() {
        LambdaQueryWrapper<PmsBrand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsBrand::getStatus, 1);
        wrapper.orderByDesc(PmsBrand::getSort);
        return list(wrapper);
    }

    @Override
    public boolean create(PmsBrand brand) {
        brand.setCreateTime(LocalDateTime.now());
        brand.setUpdateTime(LocalDateTime.now());
        return save(brand);
    }

    @Override
    public boolean update(Long id, PmsBrand brand) {
        brand.setId(id);
        brand.setUpdateTime(LocalDateTime.now());
        return updateById(brand);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public boolean deleteBatch(List<Long> ids) {
        return removeByIds(ids);
    }
}
