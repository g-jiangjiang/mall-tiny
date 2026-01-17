package com.macro.mall.tiny.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.domain.PmsSpu;
import com.macro.mall.tiny.mapper.PmsSpuMapper;
import com.macro.mall.tiny.service.PmsSpuService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PmsSpuServiceImpl extends ServiceImpl<PmsSpuMapper, PmsSpu> implements PmsSpuService {

    @Override
    public Page<PmsSpu> list(String keyword, Long categoryId, Integer pageNum, Integer pageSize) {
        Page<PmsSpu> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsSpu> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(PmsSpu::getName, keyword).or().like(PmsSpu::getBrand, keyword);
        }
        if (categoryId != null) {
            wrapper.eq(PmsSpu::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(PmsSpu::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public boolean save(PmsSpu entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return super.save(entity);
    }

    @Override
    public boolean updateById(PmsSpu entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return super.updateById(entity);
    }
}
