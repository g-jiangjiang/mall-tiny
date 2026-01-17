package com.macro.mall.tiny.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.domain.PmsProductSpec;
import com.macro.mall.tiny.domain.PmsProductSpecValue;
import com.macro.mall.tiny.dto.PmsProductSpecParam;
import com.macro.mall.tiny.dto.PmsProductSpecValueParam;
import com.macro.mall.tiny.mapper.PmsProductSpecMapper;
import com.macro.mall.tiny.mapper.PmsProductSpecValueMapper;
import com.macro.mall.tiny.service.PmsProductSpecService;
import com.macro.mall.tiny.service.PmsProductSpecValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PmsProductSpecServiceImpl extends ServiceImpl<PmsProductSpecMapper, PmsProductSpec> implements PmsProductSpecService {

    private final PmsProductSpecValueService productSpecValueService;
    private final PmsProductSpecValueMapper productSpecValueMapper;

    public PmsProductSpecServiceImpl(PmsProductSpecValueService productSpecValueService, PmsProductSpecValueMapper productSpecValueMapper) {
        this.productSpecValueService = productSpecValueService;
        this.productSpecValueMapper = productSpecValueMapper;
    }

    @Override
    public Page<PmsProductSpec> list(Long categoryId, Integer pageNum, Integer pageSize) {
        Page<PmsProductSpec> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProductSpec> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(PmsProductSpec::getCategoryId, categoryId);
        }
        wrapper.orderByAsc(PmsProductSpec::getSort);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(PmsProductSpecParam param) {
        PmsProductSpec spec = new PmsProductSpec();
        BeanUtils.copyProperties(param, spec);
        boolean success = save(spec);
        if (success && CollUtil.isNotEmpty(param.getValueList())) {
            productSpecValueService.batchCreate(param.getValueList(), spec.getId());
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long id, PmsProductSpecParam param) {
        PmsProductSpec spec = new PmsProductSpec();
        BeanUtils.copyProperties(param, spec);
        spec.setId(id);
        boolean success = updateById(spec);
        if (success && CollUtil.isNotEmpty(param.getValueList())) {
            // 删除原有的规格值
            LambdaQueryWrapper<PmsProductSpecValue> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PmsProductSpecValue::getSpecId, id);
            productSpecValueMapper.delete(wrapper);
            // 批量创建新的规格值
            productSpecValueService.batchCreate(param.getValueList(), id);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        // 删除关联的规格值
        LambdaQueryWrapper<PmsProductSpecValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsProductSpecValue::getSpecId, id);
        productSpecValueMapper.delete(wrapper);
        // 删除规格
        return removeById(id);
    }

    @Override
    public PmsProductSpec getDetail(Long id) {
        return getById(id);
    }

    @Override
    public List<PmsProductSpec> listAll() {
        LambdaQueryWrapper<PmsProductSpec> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(PmsProductSpec::getSort);
        return list(wrapper);
    }
}
