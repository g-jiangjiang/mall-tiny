package com.macro.mall.tiny.modules.pms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.dto.PmsSkuParam;
import com.macro.mall.tiny.modules.pms.dto.PmsSpuParam;
import com.macro.mall.tiny.modules.pms.mapper.PmsBrandMapper;
import com.macro.mall.tiny.modules.pms.mapper.PmsCategoryMapper;
import com.macro.mall.tiny.modules.pms.mapper.PmsSkuMapper;
import com.macro.mall.tiny.modules.pms.mapper.PmsSpuMapper;
import com.macro.mall.tiny.modules.pms.model.PmsBrand;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import com.macro.mall.tiny.modules.pms.model.PmsSku;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;
import com.macro.mall.tiny.modules.pms.service.PmsSpuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsSpuServiceImpl extends ServiceImpl<PmsSpuMapper, PmsSpu> implements PmsSpuService {

    @Autowired
    private PmsSkuMapper skuMapper;

    @Autowired
    private PmsBrandMapper brandMapper;

    @Autowired
    private PmsCategoryMapper categoryMapper;

    @Override
    public Page<PmsSpu> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<PmsSpu> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsSpu> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(PmsSpu::getName, keyword);
        }
        wrapper.orderByDesc(PmsSpu::getCreateTime);
        Page<PmsSpu> result = page(page, wrapper);
        List<PmsSpu> records = result.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            for (PmsSpu spu : records) {
                if (spu.getBrandId() != null) {
                    PmsBrand brand = brandMapper.selectById(spu.getBrandId());
                    if (brand != null) {
                        spu.setBrandName(brand.getName());
                    }
                }
                if (spu.getCategoryId() != null) {
                    PmsCategory category = categoryMapper.selectById(spu.getCategoryId());
                    if (category != null) {
                        spu.setCategoryName(category.getName());
                    }
                }
            }
        }
        return result;
    }

    @Override
    public PmsSpu getDetail(Long id) {
        PmsSpu spu = getById(id);
        if (spu == null) {
            return null;
        }
        LambdaQueryWrapper<PmsSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsSku::getSpuId, id);
        List<PmsSku> skuList = skuMapper.selectList(wrapper);
        spu.setSkuList(skuList);
        return spu;
    }

    @Override
    @Transactional
    public boolean create(PmsSpuParam spuParam) {
        PmsSpu spu = new PmsSpu();
        BeanUtils.copyProperties(spuParam, spu);
        spu.setPublishStatus(0);
        spu.setNewStatus(0);
        spu.setRecommandStatus(0);
        spu.setVerifyStatus(0);
        spu.setDeleteStatus(0);
        spu.setSale(0);
        save(spu);

        List<PmsSkuParam> skuParams = spuParam.getSkuList();
        if (CollUtil.isNotEmpty(skuParams)) {
            List<PmsSku> skuList = skuParams.stream().map(param -> {
                PmsSku sku = new PmsSku();
                BeanUtils.copyProperties(param, sku);
                sku.setSpuId(spu.getId());
                sku.setPublishStatus(0);
                sku.setDeleteStatus(0);
                sku.setSale(0);
                return sku;
            }).collect(Collectors.toList());
            skuList.forEach(skuMapper::insert);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean update(Long id, PmsSpuParam spuParam) {
        PmsSpu spu = new PmsSpu();
        BeanUtils.copyProperties(spuParam, spu);
        spu.setId(id);
        updateById(spu);

        LambdaQueryWrapper<PmsSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsSku::getSpuId, id);
        skuMapper.delete(wrapper);

        List<PmsSkuParam> skuParams = spuParam.getSkuList();
        if (CollUtil.isNotEmpty(skuParams)) {
            List<PmsSku> skuList = skuParams.stream().map(param -> {
                PmsSku sku = new PmsSku();
                BeanUtils.copyProperties(param, sku);
                sku.setSpuId(id);
                return sku;
            }).collect(Collectors.toList());
            skuList.forEach(skuMapper::insert);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        LambdaQueryWrapper<PmsSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsSku::getSpuId, id);
        skuMapper.delete(wrapper);
        return removeById(id);
    }
}
