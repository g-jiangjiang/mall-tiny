package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.dto.PmsSkuParam;
import com.macro.mall.tiny.modules.pms.mapper.PmsSkuMapper;
import com.macro.mall.tiny.modules.pms.model.PmsSku;
import com.macro.mall.tiny.modules.pms.service.PmsSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsSkuServiceImpl extends ServiceImpl<PmsSkuMapper, PmsSku> implements PmsSkuService {

    @Override
    public Page<PmsSku> list(String keyword, Long spuId, Integer pageSize, Integer pageNum) {
        Page<PmsSku> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsSku> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(PmsSku::getName, keyword).or().like(PmsSku::getSkuCode, keyword);
        }
        if (spuId != null) {
            wrapper.eq(PmsSku::getSpuId, spuId);
        }
        wrapper.orderByDesc(PmsSku::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public boolean create(PmsSkuParam skuParam) {
        PmsSku sku = new PmsSku();
        BeanUtils.copyProperties(skuParam, sku);
        sku.setPublishStatus(0);
        sku.setDeleteStatus(0);
        sku.setSale(0);
        return save(sku);
    }

    @Override
    public boolean update(Long id, PmsSkuParam skuParam) {
        PmsSku sku = new PmsSku();
        BeanUtils.copyProperties(skuParam, sku);
        sku.setId(id);
        return updateById(sku);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchCreate(Long spuId, List<PmsSkuParam> skuParams) {
        List<PmsSku> skuList = skuParams.stream().map(param -> {
            PmsSku sku = new PmsSku();
            BeanUtils.copyProperties(param, sku);
            sku.setSpuId(spuId);
            sku.setPublishStatus(0);
            sku.setDeleteStatus(0);
            sku.setSale(0);
            return sku;
        }).collect(Collectors.toList());
        return saveBatch(skuList);
    }
}
