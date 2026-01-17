package com.macro.mall.tiny.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.domain.PmsProductSku;
import com.macro.mall.tiny.dto.PmsProductSkuParam;
import com.macro.mall.tiny.mapper.PmsProductSkuMapper;
import com.macro.mall.tiny.service.PmsProductSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PmsProductSkuServiceImpl extends ServiceImpl<PmsProductSkuMapper, PmsProductSku> implements PmsProductSkuService {

    @Override
    public Page<PmsProductSku> list(Long productId, Integer pageNum, Integer pageSize) {
        Page<PmsProductSku> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProductSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsProductSku::getProductId, productId);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean create(PmsProductSkuParam param, Long productId) {
        PmsProductSku sku = new PmsProductSku();
        BeanUtils.copyProperties(param, sku);
        sku.setProductId(productId);
        return save(sku);
    }

    @Override
    public boolean update(Long id, PmsProductSkuParam param) {
        PmsProductSku sku = new PmsProductSku();
        BeanUtils.copyProperties(param, sku);
        sku.setId(id);
        return updateById(sku);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public PmsProductSku getDetail(Long id) {
        return getById(id);
    }

    @Override
    public boolean batchCreate(List<PmsProductSkuParam> paramList, Long productId) {
        if (paramList == null || paramList.isEmpty()) return false;
        List<PmsProductSku> skuList = paramList.stream().map(param -> {
            PmsProductSku sku = new PmsProductSku();
            BeanUtils.copyProperties(param, sku);
            sku.setProductId(productId);
            return sku;
        }).toList();
        return saveBatch(skuList);
    }
}
