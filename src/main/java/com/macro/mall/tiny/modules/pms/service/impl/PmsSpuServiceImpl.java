package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.dto.PmsSpuParam;
import com.macro.mall.tiny.modules.pms.mapper.PmsSpuAttributeMapper;
import com.macro.mall.tiny.modules.pms.mapper.PmsSpuMapper;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;
import com.macro.mall.tiny.modules.pms.model.PmsSpuAttribute;
import com.macro.mall.tiny.modules.pms.service.PmsSpuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品SPU Service实现类
 */
@Service
public class PmsSpuServiceImpl extends ServiceImpl<PmsSpuMapper, PmsSpu> implements PmsSpuService {

    @Autowired
    private PmsSpuAttributeMapper spuAttributeMapper;

    @Override
    public Page<PmsSpu> list(Long categoryId, Long brandId, String keyword, Integer pageSize, Integer pageNum) {
        Page<PmsSpu> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsSpu> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(PmsSpu::getCategoryId, categoryId);
        }
        if (brandId != null) {
            wrapper.eq(PmsSpu::getBrandId, brandId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(PmsSpu::getName, keyword);
        }
        wrapper.orderByDesc(PmsSpu::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional
    public boolean create(PmsSpuParam spuParam) {
        PmsSpu spu = new PmsSpu();
        BeanUtils.copyProperties(spuParam, spu);
        spu.setCreateTime(LocalDateTime.now());
        spu.setUpdateTime(LocalDateTime.now());
        if (spu.getStatus() == null) {
            spu.setStatus(1);
        }
        save(spu);

        // 保存规格关联
        if (spuParam.getAttributeIds() != null && !spuParam.getAttributeIds().isEmpty()) {
            saveSpuAttributes(spu.getId(), spuParam.getAttributeIds());
        }
        return true;
    }

    @Override
    @Transactional
    public boolean update(Long id, PmsSpuParam spuParam) {
        PmsSpu spu = new PmsSpu();
        BeanUtils.copyProperties(spuParam, spu);
        spu.setId(id);
        spu.setUpdateTime(LocalDateTime.now());
        updateById(spu);

        // 更新规格关联
        if (spuParam.getAttributeIds() != null) {
            // 删除旧关联
            spuAttributeMapper.deleteBySpuId(id);
            // 保存新关联
            if (!spuParam.getAttributeIds().isEmpty()) {
                saveSpuAttributes(id, spuParam.getAttributeIds());
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        // 删除规格关联
        spuAttributeMapper.deleteBySpuId(id);
        return removeById(id);
    }

    @Override
    public PmsSpuParam getDetail(Long id) {
        PmsSpu spu = getById(id);
        if (spu == null) {
            return null;
        }
        PmsSpuParam param = new PmsSpuParam();
        BeanUtils.copyProperties(spu, param);
        // 获取关联的规格ID列表
        List<Long> attributeIds = new ArrayList<>();
        LambdaQueryWrapper<PmsSpuAttribute> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsSpuAttribute::getSpuId, id);
        List<PmsSpuAttribute> spuAttributes = spuAttributeMapper.selectList(wrapper);
        for (PmsSpuAttribute spuAttribute : spuAttributes) {
            attributeIds.add(spuAttribute.getAttributeId());
        }
        param.setAttributeIds(attributeIds);
        return param;
    }

    @Override
    public boolean updateStatusBatch(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        for (Long id : ids) {
            PmsSpu spu = new PmsSpu();
            spu.setId(id);
            spu.setStatus(status);
            updateById(spu);
        }
        return true;
    }

    /**
     * 保存SPU规格关联
     */
    private void saveSpuAttributes(Long spuId, List<Long> attributeIds) {
        List<PmsSpuAttribute> list = new ArrayList<>();
        for (Long attributeId : attributeIds) {
            PmsSpuAttribute spuAttribute = new PmsSpuAttribute();
            spuAttribute.setSpuId(spuId);
            spuAttribute.setAttributeId(attributeId);
            list.add(spuAttribute);
        }
        spuAttributeMapper.batchInsert(list);
    }
}
