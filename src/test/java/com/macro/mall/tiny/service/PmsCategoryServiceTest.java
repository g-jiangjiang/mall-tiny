package com.macro.mall.tiny.service;

import com.macro.mall.tiny.MallTinyApplicationTests;
import com.macro.mall.tiny.dto.PmsCategoryParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PmsCategoryServiceTest extends MallTinyApplicationTests {

    @Autowired
    private PmsCategoryService categoryService;

    @Test
    public void testCreateCategory() {
        PmsCategoryParam param = new PmsCategoryParam();
        param.setName("测试类目");
        param.setParentId(0L);
        param.setSort(0);
        param.setStatus(1);
        boolean success = categoryService.create(param);
        assertTrue(success);
    }
}
