package com.macro.mall.tiny.service;

import com.macro.mall.tiny.entity.PmsSpecValue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PmsSpecValueServiceTest {

    @Autowired
    private PmsSpecValueService specValueService;

    private static Long testSpecValueId;

    @Test
    @Order(1)
    public void testCreate() {
        PmsSpecValue specValue = new PmsSpecValue();
        specValue.setSpecTypeId(1L);
        specValue.setValue("红色");
        specValue.setSort(1);

        boolean result = specValueService.create(specValue);
        assertTrue(result);
        assertNotNull(specValue.getId());
        testSpecValueId = specValue.getId();
    }

    @Test
    @Order(2)
    public void testUpdate() {
        PmsSpecValue specValue = new PmsSpecValue();
        specValue.setId(testSpecValueId);
        specValue.setValue("深红色");
        specValue.setSort(2);

        boolean result = specValueService.update(testSpecValueId, specValue);
        assertTrue(result);

        PmsSpecValue updated = specValueService.getById(testSpecValueId);
        assertEquals("深红色", updated.getValue());
    }

    @Test
    @Order(3)
    public void testBatchCreate() {
        PmsSpecValue value1 = new PmsSpecValue();
        value1.setSpecTypeId(1L);
        value1.setValue("蓝色");
        value1.setSort(3);

        PmsSpecValue value2 = new PmsSpecValue();
        value2.setSpecTypeId(1L);
        value2.setValue("绿色");
        value2.setSort(4);

        List<PmsSpecValue> specValues = Arrays.asList(value1, value2);
        boolean result = specValueService.batchCreate(specValues);
        assertTrue(result);
    }

    @Test
    @Order(4)
    public void testGetBySpecTypeId() {
        List<PmsSpecValue> specValues = specValueService.getBySpecTypeId(1L);
        assertNotNull(specValues);
    }

    @Test
    @Order(5)
    public void testGetById() {
        PmsSpecValue specValue = specValueService.getById(testSpecValueId);
        assertNotNull(specValue);
        assertEquals(testSpecValueId, specValue.getId());
    }

    @Test
    @Order(6)
    public void testDelete() {
        boolean result = specValueService.delete(testSpecValueId);
        assertTrue(result);

        PmsSpecValue deleted = specValueService.getById(testSpecValueId);
        assertNull(deleted);
    }
}
