package com.macro.mall.tiny.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.domain.PmsProduct;
import com.macro.mall.tiny.domain.PmsProductSku;
import com.macro.mall.tiny.dto.PmsProductParam;
import com.macro.mall.tiny.dto.PmsProductSkuParam;
import com.macro.mall.tiny.mapper.PmsProductMapper;
import com.macro.mall.tiny.mapper.PmsProductSkuMapper;
import com.macro.mall.tiny.service.PmsProductService;
import com.macro.mall.tiny.service.PmsProductSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {

    private final PmsProductSkuService productSkuService;
    private final PmsProductSkuMapper productSkuMapper;

    public PmsProductServiceImpl(PmsProductSkuService productSkuService, PmsProductSkuMapper productSkuMapper) {
        this.productSkuService = productSkuService;
        this.productSkuMapper = productSkuMapper;
    }

    @Override
    public Page<PmsProduct> list(String name, Long categoryId, Integer pageNum, Integer pageSize) {
        Page<PmsProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProduct> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(PmsProduct::getName, name);
        }
        if (categoryId != null) {
            wrapper.eq(PmsProduct::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(PmsProduct::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(PmsProductParam param) {
        PmsProduct product = new PmsProduct();
        BeanUtils.copyProperties(param, product);
        boolean success = save(product);
        if (success && CollUtil.isNotEmpty(param.getSkuList())) {
            productSkuService.batchCreate(param.getSkuList(), product.getId());
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long id, PmsProductParam param) {
        PmsProduct product = new PmsProduct();
        BeanUtils.copyProperties(param, product);
        product.setId(id);
        boolean success = updateById(product);
        if (success && CollUtil.isNotEmpty(param.getSkuList())) {
            // 删除原有的SKU
            LambdaQueryWrapper<PmsProductSku> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PmsProductSku::getProductId, id);
            productSkuMapper.delete(wrapper);
            // 批量创建新的SKU
            productSkuService.batchCreate(param.getSkuList(), id);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        // 删除关联的SKU
        LambdaQueryWrapper<PmsProductSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsProductSku::getProductId, id);
        productSkuMapper.delete(wrapper);
        // 删除商品
        return removeById(id);
    }

    @Override
    public PmsProduct getDetail(Long id) {
        return getById(id);
    }

    @Override
    public List<PmsProduct> listAll() {
        LambdaQueryWrapper<PmsProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PmsProduct::getCreateTime);
        return list(wrapper);
    }
}
