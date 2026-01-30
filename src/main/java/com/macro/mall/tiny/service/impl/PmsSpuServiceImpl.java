package com.macro.mall.tiny.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.entity.PmsSpu;
import com.macro.mall.tiny.mapper.PmsSpuMapper;
import com.macro.mall.tiny.service.PmsSpuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PmsSpuServiceImpl extends ServiceImpl<PmsSpuMapper, PmsSpu> implements PmsSpuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(PmsSpu spu) {
        spu.setCreateTime(LocalDateTime.now());
        spu.setUpdateTime(LocalDateTime.now());
        return this.save(spu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long id, PmsSpu spu) {
        spu.setId(id);
        spu.setUpdateTime(LocalDateTime.now());
        return this.updateById(spu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public Page<PmsSpu> list(String keyword, Long categoryId, Integer pageNum, Integer pageSize) {
        Page<PmsSpu> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsSpu> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(PmsSpu::getName, keyword);
        }
        if (categoryId != null) {
            wrapper.eq(PmsSpu::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(PmsSpu::getSort);
        return this.page(page, wrapper);
    }

    @Override
    public PmsSpu getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePublishStatus(Long id, Integer publishStatus) {
        PmsSpu spu = new PmsSpu();
        spu.setId(id);
        spu.setPublishStatus(publishStatus);
        spu.setUpdateTime(LocalDateTime.now());
        return this.updateById(spu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVerifyStatus(Long id, Integer verifyStatus) {
        PmsSpu spu = new PmsSpu();
        spu.setId(id);
        spu.setVerifyStatus(verifyStatus);
        spu.setUpdateTime(LocalDateTime.now());
        return this.updateById(spu);
    }
}
