package com.macro.mall.tiny.service;

import com.macro.mall.tiny.domain.PmsSpecType;
import com.macro.mall.tiny.domain.PmsSpecValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PmsSpecTypeServiceTest {

    @Autowired
    private PmsSpecTypeService specTypeService;

    @Autowired
    private PmsSpecValueService specValueService;

    @Test
    public void testCreateSpecType() {
        PmsSpecType specType = new PmsSpecType();
        specType.setName("颜色");
        specType.setDescription("商品颜色规格");
        assertTrue(specTypeService.save(specType));
        assertNotNull(specType.getId());
    }

    @Test
    public void testListAll() {
        List<PmsSpecType> list = specTypeService.listAll();
        assertNotNull(list);
    }

    @Test
    public void testBatchCreateSpecType() {
        PmsSpecType type1 = new PmsSpecType();
        type1.setName("尺寸");
        type1.setDescription("商品尺寸规格");

        PmsSpecType type2 = new PmsSpecType();
        type2.setName("材质");
        type2.setDescription("商品材质规格");

        assertTrue(specTypeService.saveBatch(List.of(type1, type2)));
    }

    @Test
    public void testCreateSpecValue() {
        PmsSpecType specType = new PmsSpecType();
        specType.setName("测试规格类型");
        assertTrue(specTypeService.save(specType));

        PmsSpecValue specValue = new PmsSpecValue();
        specValue.setSpecTypeId(specType.getId());
        specValue.setValue("测试值");
        specValue.setDescription("测试规格值");
        assertTrue(specValueService.save(specValue));
    }

    @Test
    public void testListByTypeId() {
        PmsSpecType specType = new PmsSpecType();
        specType.setName("测试规格类型2");
        assertTrue(specTypeService.save(specType));

        PmsSpecValue value1 = new PmsSpecValue();
        value1.setSpecTypeId(specType.getId());
        value1.setValue("值1");

        PmsSpecValue value2 = new PmsSpecValue();
        value2.setSpecTypeId(specType.getId());
        value2.setValue("值2");

        specValueService.saveBatch(List.of(value1, value2));

        List<PmsSpecValue> values = specValueService.listByTypeId(specType.getId());
        assertEquals(2, values.size());
    }
}
