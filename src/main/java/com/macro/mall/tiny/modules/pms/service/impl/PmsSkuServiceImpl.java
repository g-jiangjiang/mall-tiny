package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.pms.dto.PmsSkuBatchParam;
import com.macro.mall.tiny.modules.pms.mapper.PmsSkuAttributeValueMapper;
import com.macro.mall.tiny.modules.pms.mapper.PmsSkuMapper;
import com.macro.mall.tiny.modules.pms.model.PmsSku;
import com.macro.mall.tiny.modules.pms.model.PmsSkuAttributeValue;
import com.macro.mall.tiny.modules.pms.service.PmsSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品SKU Service实现类
 */
@Service
public class PmsSkuServiceImpl extends ServiceImpl<PmsSkuMapper, PmsSku> implements PmsSkuService {

    @Autowired
    private PmsSkuAttributeValueMapper skuAttributeValueMapper;

    @Override
    public List<PmsSku> listBySpuId(Long spuId) {
        LambdaQueryWrapper<PmsSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsSku::getSpuId, spuId);
        wrapper.orderByAsc(PmsSku::getSkuCode);
        return list(wrapper);
    }

    @Override
    @Transactional
    public boolean batchCreate(Long spuId, List<PmsSku> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        for (PmsSku sku : skuList) {
            sku.setSpuId(spuId);
            sku.setCreateTime(now);
            sku.setUpdateTime(now);
            if (sku.getStatus() == null) {
                sku.setStatus(1);
            }
        }
        return baseMapper.batchInsert(skuList) > 0;
    }

    @Override
    @Transactional
    public boolean batchUpdate(List<PmsSku> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        for (PmsSku sku : skuList) {
            sku.setUpdateTime(now);
        }
        return baseMapper.batchUpdate(skuList) > 0;
    }

    @Override
    @Transactional
    public boolean batchConfig(PmsSkuBatchParam batchParam) {
        Long spuId = batchParam.getSpuId();

        // 处理删除
        if (!CollectionUtils.isEmpty(batchParam.getDeleteSkuIds())) {
            // 删除SKU属性值关联
            skuAttributeValueMapper.deleteBySkuIds(batchParam.getDeleteSkuIds());
            // 删除SKU
            removeByIds(batchParam.getDeleteSkuIds());
        }

        // 处理新增和更新
        if (!CollectionUtils.isEmpty(batchParam.getSkuList())) {
            LocalDateTime now = LocalDateTime.now();
            List<PmsSku> insertList = new ArrayList<>();
            List<PmsSku> updateList = new ArrayList<>();
            // 记录需要处理关联表的SKU项（新增和更新的SKU）
            List<PmsSkuBatchParam.PmsSkuItem> needRelationItems = new ArrayList<>();

            for (PmsSkuBatchParam.PmsSkuItem item : batchParam.getSkuList()) {
                if (item.getId() == null) {
                    // 新增
                    PmsSku sku = new PmsSku();
                    BeanUtils.copyProperties(item, sku);
                    sku.setSpuId(spuId);
                    sku.setCreateTime(now);
                    sku.setUpdateTime(now);
                    if (sku.getStatus() == null) {
                        sku.setStatus(1);
                    }
                    insertList.add(sku);
                    needRelationItems.add(item);
                } else {
                    // 更新
                    PmsSku sku = new PmsSku();
                    BeanUtils.copyProperties(item, sku);
                    sku.setUpdateTime(now);
                    updateList.add(sku);
                    // 如果规格发生变化，需要重新建立关联
                    if (item.getSpecifications() != null) {
                        needRelationItems.add(item);
                    }
                }
            }

            // 批量新增
            if (!insertList.isEmpty()) {
                baseMapper.batchInsert(insertList);
                // 建立新增SKU的规格关联
                saveSkuAttributeRelations(insertList, needRelationItems);
            }

            // 批量更新
            if (!updateList.isEmpty()) {
                baseMapper.batchUpdate(updateList);
                // 更新SKU的规格关联
                for (PmsSku sku : updateList) {
                    // 删除旧的关联
                    skuAttributeValueMapper.deleteBySkuId(sku.getId());
                }
                // 重新建立关联
                saveSkuAttributeRelations(updateList, needRelationItems.stream()
                        .filter(item -> item.getId() != null)
                        .collect(Collectors.toList()));
            }
        }

        return true;
    }

    /**
     * 保存SKU规格属性关联
     */
    private void saveSkuAttributeRelations(List<PmsSku> skuList, List<PmsSkuBatchParam.PmsSkuItem> items) {
        // 这里需要根据业务需求实现规格属性值的关联逻辑
        // 由于规格组合是JSON格式存储，关联表的插入需要根据实际的规格属性ID和属性值ID来建立
        // 具体实现取决于前端传入的规格数据格式
        // 示例：解析specifications JSON，查询对应的attribute_id和attribute_value_id，然后插入关联表
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        // 删除SKU属性值关联
        skuAttributeValueMapper.deleteBySkuId(id);
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean deleteBatch(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        // 删除SKU属性值关联
        skuAttributeValueMapper.deleteBySkuIds(ids);
        return removeByIds(ids);
    }

    @Override
    public boolean updateStock(Long id, Integer stock) {
        PmsSku sku = new PmsSku();
        sku.setId(id);
        sku.setStock(stock);
        sku.setUpdateTime(LocalDateTime.now());
        return updateById(sku);
    }

    @Override
    public boolean updatePrice(Long id, BigDecimal price) {
        PmsSku sku = new PmsSku();
        sku.setId(id);
        sku.setPrice(price);
        sku.setUpdateTime(LocalDateTime.now());
        return updateById(sku);
    }

    @Override
    public PmsSku getDetail(Long id) {
        return getById(id);
    }
}
