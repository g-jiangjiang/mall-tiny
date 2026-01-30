package com.macro.mall.tiny.service;

import com.macro.mall.tiny.entity.PmsCategory;
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
public class PmsCategoryServiceTest {

    @Autowired
    private PmsCategoryService categoryService;

    private static Long testCategoryId;

    @Test
    @Order(1)
    public void testCreate() {
        PmsCategory category = new PmsCategory();
        category.setName("电子产品");
        category.setParentId(0L);
        category.setLevel(0);
        category.setProductCount(100);
        category.setProductUnit("件");
        category.setNavStatus(1);
        category.setShowStatus(1);
        category.setSort(1);
        category.setIcon("icon.png");
        category.setKeywords("电子,数码");
        category.setDescription("电子产品类目");

        boolean result = categoryService.create(category);
        assertTrue(result);
        assertNotNull(category.getId());
        testCategoryId = category.getId();
    }

    @Test
    @Order(2)
    public void testUpdate() {
        PmsCategory category = new PmsCategory();
        category.setId(testCategoryId);
        category.setName("电子产品-更新");
        category.setProductCount(200);

        boolean result = categoryService.update(testCategoryId, category);
        assertTrue(result);

        PmsCategory updated = categoryService.getById(testCategoryId);
        assertEquals("电子产品-更新", updated.getName());
    }

    @Test
    @Order(3)
    public void testBatchCreate() {
        PmsCategory cat1 = new PmsCategory();
        cat1.setName("手机");
        cat1.setParentId(testCategoryId);
        cat1.setLevel(1);

        PmsCategory cat2 = new PmsCategory();
        cat2.setName("电脑");
        cat2.setParentId(testCategoryId);
        cat2.setLevel(1);

        List<PmsCategory> categories = Arrays.asList(cat1, cat2);
        boolean result = categoryService.batchCreate(categories);
        assertTrue(result);
    }

    @Test
    @Order(4)
    public void testListWithTree() {
        List<PmsCategory> categories = categoryService.listWithTree();
        assertNotNull(categories);
        assertFalse(categories.isEmpty());
    }

    @Test
    @Order(5)
    public void testGetById() {
        PmsCategory category = categoryService.getById(testCategoryId);
        assertNotNull(category);
        assertEquals(testCategoryId, category.getId());
    }

    @Test
    @Order(6)
    public void testDelete() {
        boolean result = categoryService.delete(testCategoryId);
        assertTrue(result);

        PmsCategory deleted = categoryService.getById(testCategoryId);
        assertNull(deleted);
    }
}
