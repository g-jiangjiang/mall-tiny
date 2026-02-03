package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.model.PmsAttribute;
import com.macro.mall.tiny.modules.pms.model.PmsAttributeValue;

import java.util.List;

/**
 * 商品规格属性Service接口
 */
public interface PmsAttributeService extends IService<PmsAttribute> {

    /**
     * 分页查询规格
     */
    Page<PmsAttribute> list(String keyword, Integer type, Integer pageSize, Integer pageNum);

    /**
     * 创建规格
     */
    boolean create(PmsAttribute attribute);

    /**
     * 更新规格
     */
    boolean update(Long id, PmsAttribute attribute);

    /**
     * 删除规格
     */
    boolean delete(Long id);

    /**
     * 获取规格详情（包含属性值）
     */
    PmsAttribute getDetail(Long id);

    /**
     * 根据SPU ID获取规格列表
     */
    List<PmsAttribute> listBySpuId(Long spuId);

    /**
     * 添加属性值
     */
    boolean addAttributeValue(PmsAttributeValue attributeValue);

    /**
     * 更新属性值
     */
    boolean updateAttributeValue(Long id, PmsAttributeValue attributeValue);

    /**
     * 删除属性值
     */
    boolean deleteAttributeValue(Long id);

    /**
     * 获取属性值列表
     */
    List<PmsAttributeValue> listAttributeValues(Long attributeId);
}
