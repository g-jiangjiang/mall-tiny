package com.macro.mall.tiny.service;

import com.macro.mall.tiny.entity.PmsSpecType;
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
public class PmsSpecTypeServiceTest {

    @Autowired
    private PmsSpecTypeService specTypeService;

    private static Long testSpecTypeId;

    @Test
    @Order(1)
    public void testCreate() {
        PmsSpecType specType = new PmsSpecType();
        specType.setName("颜色");
        specType.setCategoryId(1L);
        specType.setSort(1);

        boolean result = specTypeService.create(specType);
        assertTrue(result);
        assertNotNull(specType.getId());
        testSpecTypeId = specType.getId();
    }

    @Test
    @Order(2)
    public void testUpdate() {
        PmsSpecType specType = new PmsSpecType();
        specType.setId(testSpecTypeId);
        specType.setName("机身颜色");
        specType.setSort(2);

        boolean result = specTypeService.update(testSpecTypeId, specType);
        assertTrue(result);

        PmsSpecType updated = specTypeService.getById(testSpecTypeId);
        assertEquals("机身颜色", updated.getName());
    }

    @Test
    @Order(3)
    public void testBatchCreate() {
        PmsSpecType type1 = new PmsSpecType();
        type1.setName("内存容量");
        type1.setCategoryId(1L);
        type1.setSort(2);

        PmsSpecType type2 = new PmsSpecType();
        type2.setName("存储容量");
        type2.setCategoryId(1L);
        type2.setSort(3);

        List<PmsSpecType> specTypes = Arrays.asList(type1, type2);
        boolean result = specTypeService.batchCreate(specTypes);
        assertTrue(result);
    }

    @Test
    @Order(4)
    public void testGetByCategoryId() {
        List<PmsSpecType> specTypes = specTypeService.getByCategoryId(1L);
        assertNotNull(specTypes);
    }

    @Test
    @Order(5)
    public void testGetById() {
        PmsSpecType specType = specTypeService.getById(testSpecTypeId);
        assertNotNull(specType);
        assertEquals(testSpecTypeId, specType.getId());
    }

    @Test
    @Order(6)
    public void testDelete() {
        boolean result = specTypeService.delete(testSpecTypeId);
        assertTrue(result);

        PmsSpecType deleted = specTypeService.getById(testSpecTypeId);
        assertNull(deleted);
    }
}
