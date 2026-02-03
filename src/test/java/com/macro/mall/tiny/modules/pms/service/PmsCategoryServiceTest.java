package com.macro.mall.tiny.modules.pms.service;

import com.macro.mall.tiny.modules.pms.dto.PmsCategoryNode;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品类目Service单元测试
 */
@SpringBootTest
@Transactional
public class PmsCategoryServiceTest {

    @Autowired
    private PmsCategoryService categoryService;

    @Test
    public void testCreateCategory() {
        PmsCategory category = new PmsCategory();
        category.setName("测试类目");
        category.setParentId(0L);
        category.setLevel(1);
        category.setSort(100);
        category.setStatus(1);

        boolean result = categoryService.create(category);
        assertTrue(result);
        assertNotNull(category.getId());
        assertEquals(1, category.getLevel());
    }

    @Test
    public void testCreateSubCategory() {
        // 创建父类目
        PmsCategory parent = new PmsCategory();
        parent.setName("父类目");
        parent.setParentId(0L);
        parent.setStatus(1);
        categoryService.create(parent);

        // 创建子类目
        PmsCategory child = new PmsCategory();
        child.setName("子类目");
        child.setParentId(parent.getId());
        child.setStatus(1);
        categoryService.create(child);

        assertEquals(2, child.getLevel());
    }

    @Test
    public void testUpdateCategory() {
        PmsCategory category = new PmsCategory();
        category.setName("测试类目");
        category.setParentId(0L);
        category.setStatus(1);
        categoryService.create(category);

        PmsCategory update = new PmsCategory();
        update.setName("更新后的类目");
        update.setDescription("更新后的描述");
        boolean result = categoryService.update(category.getId(), update);
        assertTrue(result);

        PmsCategory updated = categoryService.getById(category.getId());
        assertEquals("更新后的类目", updated.getName());
    }

    @Test
    public void testDeleteCategory() {
        PmsCategory category = new PmsCategory();
        category.setName("待删除类目");
        category.setParentId(0L);
        category.setStatus(1);
        categoryService.create(category);
        Long id = category.getId();

        boolean result = categoryService.delete(id);
        assertTrue(result);
        assertNull(categoryService.getById(id));
    }

    @Test
    public void testDeleteCategoryWithChildren() {
        // 创建父类目
        PmsCategory parent = new PmsCategory();
        parent.setName("父类目");
        parent.setParentId(0L);
        parent.setStatus(1);
        categoryService.create(parent);

        // 创建子类目
        PmsCategory child = new PmsCategory();
        child.setName("子类目");
        child.setParentId(parent.getId());
        child.setStatus(1);
        categoryService.create(child);

        // 尝试删除父类目，应该抛出异常
        assertThrows(RuntimeException.class, () -> {
            categoryService.delete(parent.getId());
        });
    }

    @Test
    public void testTree() {
        // 创建一级类目
        PmsCategory cat1 = new PmsCategory();
        cat1.setName("一级类目1");
        cat1.setParentId(0L);
        cat1.setSort(1);
        cat1.setStatus(1);
        categoryService.create(cat1);

        // 创建二级类目
        PmsCategory cat2 = new PmsCategory();
        cat2.setName("二级类目1");
        cat2.setParentId(cat1.getId());
        cat2.setSort(1);
        cat2.setStatus(1);
        categoryService.create(cat2);

        // 获取树
        List<PmsCategoryNode> tree = categoryService.tree();
        assertNotNull(tree);
        assertTrue(tree.size() > 0);

        // 验证树结构
        boolean found = false;
        for (PmsCategoryNode node : tree) {
            if (node.getName().equals("一级类目1")) {
                assertNotNull(node.getChildren());
                assertTrue(node.getChildren().size() > 0);
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testListByParentId() {
        // 创建父类目
        PmsCategory parent = new PmsCategory();
        parent.setName("父类目");
        parent.setParentId(0L);
        parent.setStatus(1);
        categoryService.create(parent);

        // 创建子类目
        for (int i = 1; i <= 3; i++) {
            PmsCategory child = new PmsCategory();
            child.setName("子类目" + i);
            child.setParentId(parent.getId());
            child.setSort(i);
            child.setStatus(1);
            categoryService.create(child);
        }

        // 查询子类目
        List<PmsCategory> children = categoryService.listByParentId(parent.getId());
        assertEquals(3, children.size());
    }
}
