package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.model.PmsBrand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品品牌Service单元测试
 */
@SpringBootTest
@Transactional
public class PmsBrandServiceTest {

    @Autowired
    private PmsBrandService brandService;

    @Test
    public void testCreateBrand() {
        PmsBrand brand = new PmsBrand();
        brand.setName("测试品牌");
        brand.setDescription("这是一个测试品牌");
        brand.setSort(100);
        brand.setStatus(1);

        boolean result = brandService.create(brand);
        assertTrue(result);
        assertNotNull(brand.getId());
    }

    @Test
    public void testUpdateBrand() {
        // 先创建
        PmsBrand brand = new PmsBrand();
        brand.setName("测试品牌");
        brand.setSort(100);
        brand.setStatus(1);
        brandService.create(brand);

        // 再更新
        PmsBrand updateBrand = new PmsBrand();
        updateBrand.setName("更新后的品牌");
        updateBrand.setDescription("更新后的描述");
        boolean result = brandService.update(brand.getId(), updateBrand);
        assertTrue(result);

        // 验证更新
        PmsBrand updated = brandService.getById(brand.getId());
        assertEquals("更新后的品牌", updated.getName());
        assertEquals("更新后的描述", updated.getDescription());
    }

    @Test
    public void testDeleteBrand() {
        // 先创建
        PmsBrand brand = new PmsBrand();
        brand.setName("待删除品牌");
        brand.setSort(100);
        brand.setStatus(1);
        brandService.create(brand);
        Long id = brand.getId();

        // 删除
        boolean result = brandService.delete(id);
        assertTrue(result);

        // 验证删除
        PmsBrand deleted = brandService.getById(id);
        assertNull(deleted);
    }

    @Test
    public void testListBrands() {
        // 创建测试数据
        for (int i = 1; i <= 5; i++) {
            PmsBrand brand = new PmsBrand();
            brand.setName("品牌" + i);
            brand.setSort(100 - i);
            brand.setStatus(1);
            brandService.create(brand);
        }

        // 分页查询
        Page<PmsBrand> page = brandService.list("品牌", 10, 1);
        assertNotNull(page);
        assertTrue(page.getTotal() >= 5);
    }

    @Test
    public void testListAllBrands() {
        // 创建测试数据
        PmsBrand brand = new PmsBrand();
        brand.setName("测试品牌");
        brand.setSort(100);
        brand.setStatus(1);
        brandService.create(brand);

        // 查询所有
        List<PmsBrand> list = brandService.listAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testBatchDeleteBrands() {
        // 创建测试数据
        PmsBrand brand1 = new PmsBrand();
        brand1.setName("品牌1");
        brand1.setStatus(1);
        brandService.create(brand1);

        PmsBrand brand2 = new PmsBrand();
        brand2.setName("品牌2");
        brand2.setStatus(1);
        brandService.create(brand2);

        // 批量删除
        List<Long> ids = Arrays.asList(brand1.getId(), brand2.getId());
        boolean result = brandService.deleteBatch(ids);
        assertTrue(result);

        // 验证删除
        assertNull(brandService.getById(brand1.getId()));
        assertNull(brandService.getById(brand2.getId()));
    }
}
