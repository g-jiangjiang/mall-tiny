package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.dto.PmsSpuParam;
import com.macro.mall.tiny.modules.pms.model.PmsAttribute;
import com.macro.mall.tiny.modules.pms.model.PmsBrand;
import com.macro.mall.tiny.modules.pms.model.PmsCategory;
import com.macro.mall.tiny.modules.pms.model.PmsSpu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品SPU Service单元测试
 */
@SpringBootTest
@Transactional
public class PmsSpuServiceTest {

    @Autowired
    private PmsSpuService spuService;

    @Autowired
    private PmsBrandService brandService;

    @Autowired
    private PmsCategoryService categoryService;

    @Autowired
    private PmsAttributeService attributeService;

    private Long brandId;
    private Long categoryId;
    private Long attributeId;

    @BeforeEach
    public void setUp() {
        // 创建品牌
        PmsBrand brand = new PmsBrand();
        brand.setName("测试品牌");
        brand.setStatus(1);
        brandService.create(brand);
        brandId = brand.getId();

        // 创建类目
        PmsCategory category = new PmsCategory();
        category.setName("测试类目");
        category.setParentId(0L);
        category.setStatus(1);
        categoryService.create(category);
        categoryId = category.getId();

        // 创建规格
        PmsAttribute attribute = new PmsAttribute();
        attribute.setName("颜色");
        attribute.setType(0);
        attribute.setStatus(1);
        attributeService.create(attribute);
        attributeId = attribute.getId();
    }

    @Test
    public void testCreateSpu() {
        PmsSpuParam param = new PmsSpuParam();
        param.setName("测试商品");
        param.setBrandId(brandId);
        param.setCategoryId(categoryId);
        param.setDetail("这是商品详情");
        param.setStatus(1);

        boolean result = spuService.create(param);
        assertTrue(result);
    }

    @Test
    public void testCreateSpuWithAttributes() {
        PmsSpuParam param = new PmsSpuParam();
        param.setName("测试商品");
        param.setBrandId(brandId);
        param.setCategoryId(categoryId);
        param.setDetail("这是商品详情");
        param.setStatus(1);
        param.setAttributeIds(Arrays.asList(attributeId));

        boolean result = spuService.create(param);
        assertTrue(result);

        // 验证规格关联
        PmsSpuParam detail = spuService.getDetail(param.getId());
        assertNotNull(detail.getAttributeIds());
        assertEquals(1, detail.getAttributeIds().size());
        assertEquals(attributeId, detail.getAttributeIds().get(0));
    }

    @Test
    public void testUpdateSpu() {
        // 创建SPU
        PmsSpuParam param = new PmsSpuParam();
        param.setName("测试商品");
        param.setBrandId(brandId);
        param.setCategoryId(categoryId);
        param.setStatus(1);
        spuService.create(param);
        Long spuId = param.getId();

        // 更新SPU
        PmsSpuParam update = new PmsSpuParam();
        update.setName("更新后的商品");
        update.setBrandId(brandId);
        update.setCategoryId(categoryId);
        update.setDetail("更新后的详情");
        boolean result = spuService.update(spuId, update);
        assertTrue(result);

        // 验证更新
        PmsSpuParam detail = spuService.getDetail(spuId);
        assertEquals("更新后的商品", detail.getName());
        assertEquals("更新后的详情", detail.getDetail());
    }

    @Test
    public void testDeleteSpu() {
        // 创建SPU
        PmsSpuParam param = new PmsSpuParam();
        param.setName("待删除商品");
        param.setBrandId(brandId);
        param.setCategoryId(categoryId);
        param.setStatus(1);
        spuService.create(param);
        Long spuId = param.getId();

        // 删除SPU
        boolean result = spuService.delete(spuId);
        assertTrue(result);

        // 验证删除
        PmsSpuParam detail = spuService.getDetail(spuId);
        assertNull(detail);
    }

    @Test
    public void testListSpus() {
        // 创建测试数据
        for (int i = 1; i <= 5; i++) {
            PmsSpuParam param = new PmsSpuParam();
            param.setName("商品" + i);
            param.setBrandId(brandId);
            param.setCategoryId(categoryId);
            param.setStatus(1);
            spuService.create(param);
        }

        // 分页查询
        Page<PmsSpu> page = spuService.list(null, null, "商品", 10, 1);
        assertNotNull(page);
        assertTrue(page.getTotal() >= 5);

        // 按类目查询
        Page<PmsSpu> pageByCategory = spuService.list(categoryId, null, null, 10, 1);
        assertTrue(pageByCategory.getTotal() >= 5);

        // 按品牌查询
        Page<PmsSpu> pageByBrand = spuService.list(null, brandId, null, 10, 1);
        assertTrue(pageByBrand.getTotal() >= 5);
    }

    @Test
    public void testUpdateStatusBatch() {
        // 创建测试数据
        List<Long> ids = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            PmsSpuParam param = new PmsSpuParam();
            param.setName("商品" + i);
            param.setBrandId(brandId);
            param.setCategoryId(categoryId);
            param.setStatus(1);
            spuService.create(param);
            ids.add(param.getId());
        }

        // 批量下架
        boolean result = spuService.updateStatusBatch(ids, 0);
        assertTrue(result);

        // 验证状态
        for (Long id : ids) {
            PmsSpuParam detail = spuService.getDetail(id);
            assertEquals(0, detail.getStatus());
        }

        // 批量上架
        result = spuService.updateStatusBatch(ids, 1);
        assertTrue(result);

        // 验证状态
        for (Long id : ids) {
            PmsSpuParam detail = spuService.getDetail(id);
            assertEquals(1, detail.getStatus());
        }
    }

    @Test
    public void testGetDetail() {
        // 创建SPU
        PmsSpuParam param = new PmsSpuParam();
        param.setName("测试商品");
        param.setBrandId(brandId);
        param.setCategoryId(categoryId);
        param.setDetail("商品详情内容");
        param.setMainImage("http://example.com/main.jpg");
        param.setSubImages("http://example.com/sub1.jpg,http://example.com/sub2.jpg");
        param.setStatus(1);
        param.setAttributeIds(Arrays.asList(attributeId));
        spuService.create(param);

        // 获取详情
        PmsSpuParam detail = spuService.getDetail(param.getId());
        assertNotNull(detail);
        assertEquals("测试商品", detail.getName());
        assertEquals(brandId, detail.getBrandId());
        assertEquals(categoryId, detail.getCategoryId());
        assertEquals("商品详情内容", detail.getDetail());
        assertEquals("http://example.com/main.jpg", detail.getMainImage());
        assertNotNull(detail.getAttributeIds());
    }
}
