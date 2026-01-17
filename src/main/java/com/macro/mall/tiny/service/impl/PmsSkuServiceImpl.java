package com.macro.mall.tiny.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.domain.PmsSku;
import com.macro.mall.tiny.mapper.PmsSkuMapper;
import com.macro.mall.tiny.service.PmsSkuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PmsSkuServiceImpl extends ServiceImpl<PmsSkuMapper, PmsSku> implements PmsSkuService {

    @Override
    public Page<PmsSku> list(Long spuId, String code, Integer pageNum, Integer pageSize) {
        Page<PmsSku> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsSku> wrapper = new LambdaQueryWrapper<>();
        if (spuId != null) {
            wrapper.eq(PmsSku::getSpuId, spuId);
        }
        if (StrUtil.isNotBlank(code)) {
            wrapper.like(PmsSku::getCode, code);
        }
        wrapper.orderByDesc(PmsSku::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional
    public boolean batchCreate(List<PmsSku> skus) {
        LocalDateTime now = LocalDateTime.now();
        skus.forEach(sku -> {
            sku.setCreateTime(now);
            sku.setUpdateTime(now);
        });
        return saveBatch(skus);
    }

    @Override
    public boolean save(PmsSku entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return super.save(entity);
    }

    @Override
    public boolean updateById(PmsSku entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return super.updateById(entity);
    }
}
