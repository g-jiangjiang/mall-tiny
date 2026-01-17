package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.dto.PmsCategoryParam;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
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
    public void testListWithChildren() {
        List<PmsCategory> categories = categoryService.listWithChildren();
        assertNotNull(categories);
    }

    @Test
    public void testCreateCategory() {
        PmsCategoryParam param = new PmsCategoryParam();
        param.setName("测试类目");
        param.setParentId(0L);
        param.setLevel(0);
        param.setNavStatus(1);
        param.setShowStatus(1);
        param.setSort(0);

        boolean result = categoryService.create(param);
        assertTrue(result);
    }

    @Test
    public void testUpdateCategory() {
        PmsCategoryParam param = new PmsCategoryParam();
        param.setName("更新后的类目");
        param.setNavStatus(1);
        param.setShowStatus(1);
        param.setSort(1);

        boolean result = categoryService.update(1L, param);
        assertTrue(result);
    }

    @Test
    public void testDeleteCategory() {
        boolean result = categoryService.delete(999L);
        assertFalse(result);
    }
}
