package com.macro.mall.tiny.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.entity.PmsSku;
import com.macro.mall.tiny.mapper.PmsSkuMapper;
import com.macro.mall.tiny.service.PmsSkuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PmsSkuServiceImpl extends ServiceImpl<PmsSkuMapper, PmsSku> implements PmsSkuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(PmsSku sku) {
        sku.setCreateTime(LocalDateTime.now());
        sku.setUpdateTime(LocalDateTime.now());
        return this.save(sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long id, PmsSku sku) {
        sku.setId(id);
        sku.setUpdateTime(LocalDateTime.now());
        return this.updateById(sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public Page<PmsSku> list(Long spuId, Integer pageNum, Integer pageSize) {
        Page<PmsSku> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsSku> wrapper = new LambdaQueryWrapper<>();
        if (spuId != null) {
            wrapper.eq(PmsSku::getSpuId, spuId);
        }
        return this.page(page, wrapper);
    }

    @Override
    public List<PmsSku> getBySpuId(Long spuId) {
        return this.list(new LambdaQueryWrapper<PmsSku>()
                .eq(PmsSku::getSpuId, spuId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreate(List<PmsSku> skuList) {
        LocalDateTime now = LocalDateTime.now();
        for (PmsSku sku : skuList) {
            sku.setCreateTime(now);
            sku.setUpdateTime(now);
        }
        return this.saveBatch(skuList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<PmsSku> skuList) {
        LocalDateTime now = LocalDateTime.now();
        for (PmsSku sku : skuList) {
            sku.setUpdateTime(now);
        }
        return this.updateBatchById(skuList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStock(Long id, Integer stock) {
        PmsSku sku = new PmsSku();
        sku.setId(id);
        sku.setStock(stock);
        sku.setUpdateTime(LocalDateTime.now());
        return this.updateById(sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePrice(Long id, BigDecimal price) {
        PmsSku sku = new PmsSku();
        sku.setId(id);
        sku.setPrice(price);
        sku.setUpdateTime(LocalDateTime.now());
        return this.updateById(sku);
    }
}
