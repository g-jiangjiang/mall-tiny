package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.domain.PmsCategory;
import com.macro.mall.tiny.modules.pms.domain.PmsSpu;
import com.macro.mall.tiny.modules.pms.domain.PmsSku;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PmsSpuServiceTest {

    @Autowired
    private PmsSpuService spuService;

    @Autowired
    private PmsSkuService skuService;

    @Autowired
    private PmsCategoryService categoryService;

    @Test
    public void testCreateSpu() {
        PmsSpu spu = new PmsSpu();
        spu.setName("测试商品");
        spu.setBrand("测试品牌");
        spu.setCategoryId(1L);
        spu.setDetail("这是一个测试商品详情");
        assertTrue(spuService.save(spu));
        assertNotNull(spu.getId());
    }

    @Test
    public void testListSpu() {
        Page<PmsSpu> page = spuService.list(null, null, 1, 10);
        assertNotNull(page);
        assertNotNull(page.getRecords());
    }

    @Test
    public void testCreateSpuWithCategory() {
        PmsCategory category = new PmsCategory();
        category.setName("测试商品类目");
        category.setParentId(0L);
        category.setLevel(0);
        assertTrue(categoryService.save(category));

        PmsSpu spu = new PmsSpu();
        spu.setName("带类目测试商品");
        spu.setBrand("测试品牌");
        spu.setCategoryId(category.getId());
        spu.setDetail("带类目测试商品详情");
        assertTrue(spuService.save(spu));
    }

    @Test
    public void testCreateSku() {
        PmsSpu spu = new PmsSpu();
        spu.setName("SKU测试商品");
        spu.setBrand("测试品牌");
        spu.setCategoryId(1L);
        spu.setDetail("SKU测试商品详情");
        assertTrue(spuService.save(spu));

        PmsSku sku = new PmsSku();
        sku.setCode("TEST-SKU-001");
        sku.setSpuId(spu.getId());
        sku.setPrice(new BigDecimal(99.99));
        sku.setStock(100);
        sku.setSpecs("颜色:红色,尺寸:L");
        assertTrue(skuService.save(sku));
    }

    @Test
    public void testBatchCreateSku() {
        PmsSpu spu = new PmsSpu();
        spu.setName("批量SKU测试商品");
        spu.setBrand("测试品牌");
        spu.setCategoryId(1L);
        spu.setDetail("批量SKU测试商品详情");
        assertTrue(spuService.save(spu));

        PmsSku sku1 = new PmsSku();
        sku1.setCode("BATCH-SKU-001");
        sku1.setSpuId(spu.getId());
        sku1.setPrice(new BigDecimal(99.99));
        sku1.setStock(100);
        sku1.setSpecs("颜色:红色,尺寸:S");

        PmsSku sku2 = new PmsSku();
        sku2.setCode("BATCH-SKU-002");
        sku2.setSpuId(spu.getId());
        sku2.setPrice(new BigDecimal(109.99));
        sku2.setStock(200);
        sku2.setSpecs("颜色:蓝色,尺寸:M");

        PmsSku sku3 = new PmsSku();
        sku3.setCode("BATCH-SKU-003");
        sku3.setSpuId(spu.getId());
        sku3.setPrice(new BigDecimal(119.99));
        sku3.setStock(150);
        sku3.setSpecs("颜色:绿色,尺寸:L");

        assertTrue(skuService.batchCreate(List.of(sku1, sku2, sku3)));
    }

    @Test
    public void testListSku() {
        Page<PmsSku> page = skuService.list(null, null, 1, 10);
        assertNotNull(page);
        assertNotNull(page.getRecords());
    }
}
