package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.model.PmsAttribute;
import com.macro.mall.tiny.modules.pms.model.PmsAttributeValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品规格Service单元测试
 */
@SpringBootTest
@Transactional
public class PmsAttributeServiceTest {

    @Autowired
    private PmsAttributeService attributeService;

    @Test
    public void testCreateAttribute() {
        PmsAttribute attribute = new PmsAttribute();
        attribute.setName("颜色");
        attribute.setType(0);
        attribute.setInputType(1);
        attribute.setSelectList("红色,蓝色,黑色");
        attribute.setSort(100);
        attribute.setStatus(1);

        boolean result = attributeService.create(attribute);
        assertTrue(result);
        assertNotNull(attribute.getId());
    }

    @Test
    public void testUpdateAttribute() {
        PmsAttribute attribute = new PmsAttribute();
        attribute.setName("颜色");
        attribute.setType(0);
        attribute.setStatus(1);
        attributeService.create(attribute);

        PmsAttribute update = new PmsAttribute();
        update.setName("颜色（更新）");
        update.setSelectList("红色,蓝色,黑色,白色");
        boolean result = attributeService.update(attribute.getId(), update);
        assertTrue(result);

        PmsAttribute updated = attributeService.getDetail(attribute.getId());
        assertEquals("颜色（更新）", updated.getName());
    }

    @Test
    public void testDeleteAttribute() {
        PmsAttribute attribute = new PmsAttribute();
        attribute.setName("待删除规格");
        attribute.setType(0);
        attribute.setStatus(1);
        attributeService.create(attribute);
        Long id = attribute.getId();

        boolean result = attributeService.delete(id);
        assertTrue(result);
        assertNull(attributeService.getDetail(id));
    }

    @Test
    public void testListAttributes() {
        // 创建测试数据
        for (int i = 1; i <= 5; i++) {
            PmsAttribute attribute = new PmsAttribute();
            attribute.setName("规格" + i);
            attribute.setType(i % 2);
            attribute.setSort(100 - i);
            attribute.setStatus(1);
            attributeService.create(attribute);
        }

        // 分页查询
        Page<PmsAttribute> page = attributeService.list("规格", null, 10, 1);
        assertNotNull(page);
        assertTrue(page.getTotal() >= 5);

        // 按类型查询
        Page<PmsAttribute> page0 = attributeService.list(null, 0, 10, 1);
        Page<PmsAttribute> page1 = attributeService.list(null, 1, 10, 1);
        assertTrue(page0.getTotal() > 0);
        assertTrue(page1.getTotal() > 0);
    }

    @Test
    public void testAddAttributeValue() {
        // 创建规格
        PmsAttribute attribute = new PmsAttribute();
        attribute.setName("颜色");
        attribute.setType(0);
        attribute.setStatus(1);
        attributeService.create(attribute);

        // 添加属性值
        PmsAttributeValue value = new PmsAttributeValue();
        value.setAttributeId(attribute.getId());
        value.setValue("红色");
        value.setSort(1);
        value.setStatus(1);

        boolean result = attributeService.addAttributeValue(value);
        assertTrue(result);
        assertNotNull(value.getId());
    }

    @Test
    public void testListAttributeValues() {
        // 创建规格
        PmsAttribute attribute = new PmsAttribute();
        attribute.setName("尺码");
        attribute.setType(0);
        attribute.setStatus(1);
        attributeService.create(attribute);

        // 添加属性值
        String[] sizes = {"S", "M", "L", "XL"};
        for (int i = 0; i < sizes.length; i++) {
            PmsAttributeValue value = new PmsAttributeValue();
            value.setAttributeId(attribute.getId());
            value.setValue(sizes[i]);
            value.setSort(i);
            value.setStatus(1);
            attributeService.addAttributeValue(value);
        }

        // 查询属性值
        List<PmsAttributeValue> values = attributeService.listAttributeValues(attribute.getId());
        assertEquals(4, values.size());
    }

    @Test
    public void testUpdateAttributeValue() {
        // 创建规格和属性值
        PmsAttribute attribute = new PmsAttribute();
        attribute.setName("颜色");
        attribute.setType(0);
        attribute.setStatus(1);
        attributeService.create(attribute);

        PmsAttributeValue value = new PmsAttributeValue();
        value.setAttributeId(attribute.getId());
        value.setValue("红色");
        value.setStatus(1);
        attributeService.addAttributeValue(value);

        // 更新属性值
        PmsAttributeValue update = new PmsAttributeValue();
        update.setValue("深红色");
        update.setSort(10);
        boolean result = attributeService.updateAttributeValue(value.getId(), update);
        assertTrue(result);

        // 验证更新
        List<PmsAttributeValue> values = attributeService.listAttributeValues(attribute.getId());
        assertEquals("深红色", values.get(0).getValue());
    }

    @Test
    public void testDeleteAttributeValue() {
        // 创建规格和属性值
        PmsAttribute attribute = new PmsAttribute();
        attribute.setName("颜色");
        attribute.setType(0);
        attribute.setStatus(1);
        attributeService.create(attribute);

        PmsAttributeValue value = new PmsAttributeValue();
        value.setAttributeId(attribute.getId());
        value.setValue("红色");
        value.setStatus(1);
        attributeService.addAttributeValue(value);
        Long valueId = value.getId();

        // 删除属性值
        boolean result = attributeService.deleteAttributeValue(valueId);
        assertTrue(result);

        // 验证删除
        List<PmsAttributeValue> values = attributeService.listAttributeValues(attribute.getId());
        assertTrue(values.isEmpty());
    }
}
