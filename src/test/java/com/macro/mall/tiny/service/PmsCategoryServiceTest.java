package com.macro.mall.tiny.service;

import com.macro.mall.tiny.domain.PmsCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PmsCategoryServiceTest {

    @Autowired
    private PmsCategoryService categoryService;

    @Test
    public void testCreateCategory() {
        PmsCategory category = new PmsCategory();
        category.setName("测试类目");
        category.setParentId(0L);
        category.setLevel(0);
        category.setDescription("测试类目描述");
        assertTrue(categoryService.save(category));
        assertNotNull(category.getId());
    }

    @Test
    public void testTreeList() {
        List<PmsCategory> treeList = categoryService.treeList();
        assertNotNull(treeList);
    }

    @Test
    public void testBatchCreate() {
        PmsCategory parent = new PmsCategory();
        parent.setName("批量测试父类目");
        parent.setParentId(0L);
        parent.setLevel(0);
        assertTrue(categoryService.save(parent));

        PmsCategory child1 = new PmsCategory();
        child1.setName("批量测试子类目1");
        child1.setParentId(parent.getId());
        child1.setLevel(1);

        PmsCategory child2 = new PmsCategory();
        child2.setName("批量测试子类目2");
        child2.setParentId(parent.getId());
        child2.setLevel(1);

        assertTrue(categoryService.saveBatch(List.of(child1, child2)));
    }
}
