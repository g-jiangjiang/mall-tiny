package com.macro.mall.tiny.modules.pms.service;

import com.macro.mall.tiny.modules.pms.dto.PmsSkuBatchParam;
import com.macro.mall.tiny.modules.pms.dto.PmsSpuParam;
import com.macro.mall.tiny.modules.pms.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品SKU Service单元测试
 */
@SpringBootTest
@Transactional
public class PmsSkuServiceTest {

    @Autowired
    private PmsSkuService skuService;

    @Autowired
    private PmsSpuService spuService;

    @Autowired
    private PmsBrandService brandService;

    @Autowired
    private PmsCategoryService categoryService;

    private Long spuId;

    @BeforeEach
    public void setUp() {
        // 创建品牌
        PmsBrand brand = new PmsBrand();
        brand.setName("测试品牌");
        brand.setStatus(1);
        brandService.create(brand);

        // 创建类目
        PmsCategory category = new PmsCategory();
        category.setName("测试类目");
        category.setParentId(0L);
        category.setStatus(1);
        categoryService.create(category);

        // 创建SPU
        PmsSpuParam param = new PmsSpuParam();
        param.setName("测试商品");
        param.setBrandId(brand.getId());
        param.setCategoryId(category.getId());
        param.setStatus(1);
        spuService.create(param);
        spuId = param.getId();
    }

    @Test
    public void testBatchCreate() {
        List<PmsSku> skuList = new ArrayList<>();

        PmsSku sku1 = new PmsSku();
        sku1.setSkuCode("SKU001");
        sku1.setPrice(new BigDecimal("99.99"));
        sku1.setStock(100);
        sku1.setSpecifications("{\"颜色\":\"红色\"}");
        sku1.setStatus(1);
        skuList.add(sku1);

        PmsSku sku2 = new PmsSku();
        sku2.setSkuCode("SKU002");
        sku2.setPrice(new BigDecimal("109.99"));
        sku2.setStock(200);
        sku2.setSpecifications("{\"颜色\":\"蓝色\"}");
        sku2.setStatus(1);
        skuList.add(sku2);

        boolean result = skuService.batchCreate(spuId, skuList);
        assertTrue(result);

        // 验证创建
        List<PmsSku> list = skuService.listBySpuId(spuId);
        assertEquals(2, list.size());
    }

    @Test
    public void testListBySpuId() {
        // 创建SKU
        List<PmsSku> skuList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            PmsSku sku = new PmsSku();
            sku.setSkuCode("SKU00" + i);
            sku.setPrice(new BigDecimal("99.99"));
            sku.setStock(100);
            sku.setStatus(1);
            skuList.add(sku);
        }
        skuService.batchCreate(spuId, skuList);

        // 查询
        List<PmsSku> list = skuService.listBySpuId(spuId);
        assertEquals(3, list.size());
    }

    @Test
    public void testBatchUpdate() {
        // 创建SKU
        List<PmsSku> skuList = new ArrayList<>();
        PmsSku sku = new PmsSku();
        sku.setSkuCode("SKU001");
        sku.setPrice(new BigDecimal("99.99"));
        sku.setStock(100);
        sku.setStatus(1);
        skuList.add(sku);
        skuService.batchCreate(spuId, skuList);

        // 获取创建的SKU ID
        List<PmsSku> createdList = skuService.listBySpuId(spuId);
        Long skuId = createdList.get(0).getId();

        // 更新
        List<PmsSku> updateList = new ArrayList<>();
        PmsSku updateSku = new PmsSku();
        updateSku.setId(skuId);
        updateSku.setPrice(new BigDecimal("89.99"));
        updateSku.setStock(150);
        updateList.add(updateSku);

        boolean result = skuService.batchUpdate(updateList);
        assertTrue(result);

        // 验证更新
        PmsSku updated = skuService.getDetail(skuId);
        assertEquals(new BigDecimal("89.99"), updated.getPrice());
        assertEquals(150, updated.getStock());
    }

    @Test
    public void testBatchConfig() {
        // 创建初始SKU
        List<PmsSku> skuList = new ArrayList<>();
        PmsSku sku1 = new PmsSku();
        sku1.setSkuCode("SKU001");
        sku1.setPrice(new BigDecimal("99.99"));
        sku1.setStock(100);
        sku1.setStatus(1);
        skuList.add(sku1);
        skuService.batchCreate(spuId, skuList);

        Long skuId = skuService.listBySpuId(spuId).get(0).getId();

        // 批量配置：更新一个、新增一个、删除一个（模拟）
        PmsSkuBatchParam batchParam = new PmsSkuBatchParam();
        batchParam.setSpuId(spuId);

        // 更新的SKU
        List<PmsSkuBatchParam.PmsSkuItem> items = new ArrayList<>();
        PmsSkuBatchParam.PmsSkuItem updateItem = new PmsSkuBatchParam.PmsSkuItem();
        updateItem.setId(skuId);
        updateItem.setSkuCode("SKU001");
        updateItem.setPrice(new BigDecimal("79.99"));
        updateItem.setStock(50);
        items.add(updateItem);

        // 新增的SKU
        PmsSkuBatchParam.PmsSkuItem newItem = new PmsSkuBatchParam.PmsSkuItem();
        newItem.setSkuCode("SKU002");
        newItem.setPrice(new BigDecimal("109.99"));
        newItem.setStock(200);
        items.add(newItem);

        batchParam.setSkuList(items);

        boolean result = skuService.batchConfig(batchParam);
        assertTrue(result);

        // 验证
        List<PmsSku> list = skuService.listBySpuId(spuId);
        assertEquals(2, list.size());

        // 验证更新
        PmsSku updated = skuService.getDetail(skuId);
        assertEquals(new BigDecimal("79.99"), updated.getPrice());
    }

    @Test
    public void testDelete() {
        // 创建SKU
        PmsSku sku = new PmsSku();
        sku.setSkuCode("SKU001");
        sku.setPrice(new BigDecimal("99.99"));
        sku.setStock(100);
        sku.setStatus(1);

        List<PmsSku> skuList = new ArrayList<>();
        skuList.add(sku);
        skuService.batchCreate(spuId, skuList);

        Long skuId = skuService.listBySpuId(spuId).get(0).getId();

        // 删除
        boolean result = skuService.delete(skuId);
        assertTrue(result);

        // 验证删除
        PmsSku deleted = skuService.getDetail(skuId);
        assertNull(deleted);
    }

    @Test
    public void testDeleteBatch() {
        // 创建SKU
        List<PmsSku> skuList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            PmsSku sku = new PmsSku();
            sku.setSkuCode("SKU00" + i);
            sku.setPrice(new BigDecimal("99.99"));
            sku.setStock(100);
            sku.setStatus(1);
            skuList.add(sku);
        }
        skuService.batchCreate(spuId, skuList);

        List<PmsSku> createdList = skuService.listBySpuId(spuId);
        List<Long> ids = Arrays.asList(createdList.get(0).getId(), createdList.get(1).getId());

        // 批量删除
        boolean result = skuService.deleteBatch(ids);
        assertTrue(result);

        // 验证删除
        List<PmsSku> list = skuService.listBySpuId(spuId);
        assertEquals(1, list.size());
    }

    @Test
    public void testUpdateStock() {
        // 创建SKU
        PmsSku sku = new PmsSku();
        sku.setSkuCode("SKU001");
        sku.setPrice(new BigDecimal("99.99"));
        sku.setStock(100);
        sku.setStatus(1);

        List<PmsSku> skuList = new ArrayList<>();
        skuList.add(sku);
        skuService.batchCreate(spuId, skuList);

        Long skuId = skuService.listBySpuId(spuId).get(0).getId();

        // 更新库存
        boolean result = skuService.updateStock(skuId, 500);
        assertTrue(result);

        // 验证
        PmsSku updated = skuService.getDetail(skuId);
        assertEquals(500, updated.getStock());
    }

    @Test
    public void testUpdatePrice() {
        // 创建SKU
        PmsSku sku = new PmsSku();
        sku.setSkuCode("SKU001");
        sku.setPrice(new BigDecimal("99.99"));
        sku.setStock(100);
        sku.setStatus(1);

        List<PmsSku> skuList = new ArrayList<>();
        skuList.add(sku);
        skuService.batchCreate(spuId, skuList);

        Long skuId = skuService.listBySpuId(spuId).get(0).getId();

        // 更新价格
        boolean result = skuService.updatePrice(skuId, new BigDecimal("129.99"));
        assertTrue(result);

        // 验证
        PmsSku updated = skuService.getDetail(skuId);
        assertEquals(new BigDecimal("129.99"), updated.getPrice());
    }

    @Test
    public void testGetDetail() {
        // 创建SKU
        PmsSku sku = new PmsSku();
        sku.setSkuCode("SKU001");
        sku.setPrice(new BigDecimal("99.99"));
        sku.setStock(100);
        sku.setSpecifications("{\"颜色\":\"红色\",\"尺码\":\"XL\"}");
        sku.setMainImage("http://example.com/sku.jpg");
        sku.setStatus(1);

        List<PmsSku> skuList = new ArrayList<>();
        skuList.add(sku);
        skuService.batchCreate(spuId, skuList);

        Long skuId = skuService.listBySpuId(spuId).get(0).getId();

        // 获取详情
        PmsSku detail = skuService.getDetail(skuId);
        assertNotNull(detail);
        assertEquals("SKU001", detail.getSkuCode());
        assertEquals(new BigDecimal("99.99"), detail.getPrice());
        assertEquals(100, detail.getStock());
        assertEquals("{\"颜色\":\"红色\",\"尺码\":\"XL\"}", detail.getSpecifications());
    }
}
