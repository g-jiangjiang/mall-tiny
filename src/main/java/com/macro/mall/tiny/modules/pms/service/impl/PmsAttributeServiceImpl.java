package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.mapper.PmsAttributeMapper;
import com.macro.mall.tiny.modules.pms.mapper.PmsAttributeValueMapper;
import com.macro.mall.tiny.modules.pms.model.PmsAttribute;
import com.macro.mall.tiny.modules.pms.model.PmsAttributeValue;
import com.macro.mall.tiny.modules.pms.service.PmsAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品规格属性Service实现类
 */
@Service
public class PmsAttributeServiceImpl extends ServiceImpl<PmsAttributeMapper, PmsAttribute> implements PmsAttributeService {

    @Autowired
    private PmsAttributeValueMapper attributeValueMapper;

    @Override
    public Page<PmsAttribute> list(String keyword, Integer type, Integer pageSize, Integer pageNum) {
        Page<PmsAttribute> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsAttribute> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(PmsAttribute::getName, keyword);
        }
        if (type != null) {
            wrapper.eq(PmsAttribute::getType, type);
        }
        wrapper.orderByDesc(PmsAttribute::getSort);
        return page(page, wrapper);
    }

    @Override
    public boolean create(PmsAttribute attribute) {
        attribute.setCreateTime(LocalDateTime.now());
        attribute.setUpdateTime(LocalDateTime.now());
        return save(attribute);
    }

    @Override
    public boolean update(Long id, PmsAttribute attribute) {
        attribute.setId(id);
        attribute.setUpdateTime(LocalDateTime.now());
        return updateById(attribute);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        // 删除关联的属性值
        LambdaQueryWrapper<PmsAttributeValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsAttributeValue::getAttributeId, id);
        attributeValueMapper.delete(wrapper);
        return removeById(id);
    }

    @Override
    public PmsAttribute getDetail(Long id) {
        return getById(id);
    }

    @Override
    public List<PmsAttribute> listBySpuId(Long spuId) {
        return baseMapper.selectBySpuId(spuId);
    }

    @Override
    public boolean addAttributeValue(PmsAttributeValue attributeValue) {
        attributeValue.setCreateTime(LocalDateTime.now());
        return attributeValueMapper.insert(attributeValue) > 0;
    }

    @Override
    public boolean updateAttributeValue(Long id, PmsAttributeValue attributeValue) {
        attributeValue.setId(id);
        return attributeValueMapper.updateById(attributeValue) > 0;
    }

    @Override
    public boolean deleteAttributeValue(Long id) {
        return attributeValueMapper.deleteById(id) > 0;
    }

    @Override
    public List<PmsAttributeValue> listAttributeValues(Long attributeId) {
        return attributeValueMapper.selectByAttributeId(attributeId);
    }
}
