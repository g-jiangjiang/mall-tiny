package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.model.PmsBrand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PmsBrandServiceTest {

    @Autowired
    private PmsBrandService brandService;

    @Test
    public void testListBrand() {
        Page<PmsBrand> page = brandService.list(null, 5, 1);
        assertNotNull(page);
    }

    @Test
    public void testCreateBrand() {
        PmsBrand brand = new PmsBrand();
        brand.setName("测试品牌");
        brand.setFirstLetter("T");
        brand.setLogo("http://example.com/logo.jpg");
        brand.setBigPic("http://example.com/bigpic.jpg");
        brand.setBrandStory("品牌故事");
        brand.setSort(0);
        brand.setShowStatus(1);
        brand.setFactoryStatus(1);
        brand.setCreateTime(new Date());

        boolean result = brandService.create(brand);
        assertTrue(result);
    }

    @Test
    public void testUpdateBrand() {
        PmsBrand brand = new PmsBrand();
        brand.setName("更新后的品牌");
        brand.setFirstLetter("U");
        brand.setSort(1);
        brand.setShowStatus(1);

        boolean result = brandService.update(1L, brand);
        assertTrue(result);
    }

    @Test
    public void testDeleteBrand() {
        boolean result = brandService.delete(999L);
        assertTrue(result);
    }
}
